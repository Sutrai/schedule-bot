package bot.schedulebot.service.bot;

import bot.schedulebot.dao.UserDao;
import bot.schedulebot.domain.constant.ConstantsBot;
import bot.schedulebot.domain.dto.user.User;
import bot.schedulebot.service.file.ParseFileService;
import bot.schedulebot.util.KeyBoardUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotService {

    private final MessageService messageService;
    private final UserDao userDao;

    @Transactional
    public BotApiMethod<?> handleScheduleSelection(long userId, long messageId) {
        User user = getUser(userId);

        if (user.getGroupId() != null) {
            log.info("User ID \"{}\" is requesting schedule for group ID \"{}\"", user.getTelegramId(), user.getGroupId());
            return updateUserGroupAndSendSchedule(user, user.getGroupId(), userId, messageId);
        } else {
            log.warn("User ID \"{}\" has no group ID assigned. Prompting to select a group.", user.getTelegramId());
            return messageService.executeEditMessageText(ConstantsBot.FIRST_AND_SCHEDULE_TEXT, userId, messageId, KeyBoardUtils.choiceGroup());
        }
    }

    public User getUser(long userId) {
        log.info("Retrieving user with ID: {}", userId);
        return userDao.findByUserId(userId);
    }

    public BotApiMethod<?> updateUserGroupAndSendSchedule(User user, int groupId, Long chatId, Long messageId) {
        log.info("Updating user ID \"{}\" with group ID \"{}\"", user.getTelegramId(), groupId);
        List<String> rasp = ParseFileService.parseExcel(user.getSelectedFile(), groupId);
        return messageService.executeEditMessageText(formatDataText(rasp), chatId, messageId, KeyBoardUtils.implementationOfMondayMarkup());
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", new Locale("ru"));
        return date.format(formatter);
    }

    public String formatDataText(List<String> rasp) {
        return String.format(ConstantsBot.SCHEDULE_MENU_FIRST, rasp.get(rasp.size() - 1).trim(), rasp.get(rasp.size() - 2).trim());
    }

    public BotApiMethod<?> handleDaySchedule(int dayOffset, long userId, long messageId) {
        User user = getUser(userId);
        String dateData = ParseFileService.getTime(user.getSelectedFile());

        LocalDate startDate = parseStartDate(dateData);
        LocalDate targetDate = startDate.plusDays(dayOffset / 4);

        String formattedDate = formatDate(targetDate);
        String formattedDateCapitalized = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);

        log.info("User ID \"{}\" is requesting schedule for the day: {}", user.getTelegramId(), formattedDateCapitalized);
        return sendScheduleForDay(user, dayOffset, userId, messageId, formattedDateCapitalized);
    }

    public BotApiMethod<?> sendScheduleForDay(User user, int dayOffset, Long chatId, Long messageId, String formattedDate) {
        log.info("Sending schedule for user ID \"{}\" on date: {}", user.getTelegramId(), formattedDate);
        List<String> rasp = ParseFileService.parseExcel(user.getSelectedFile(), user.getGroupId());
        String scheduleText = formatScheduleText(dayOffset, rasp);

        String textWithDate = String.format("*%s%n*\n%s", formattedDate, scheduleText);
        return messageService.executeEditMessageText(textWithDate, chatId, messageId, KeyBoardUtils.implementationOfMondayMarkup());
    }

    public String formatScheduleText(int startIndex, List<String> rasp) {
        StringBuilder schedule = new StringBuilder();
        log.debug("Formatting schedule text from index: {}", startIndex);

        for (int i = 0; i < 4; i++) {
            String line = rasp.get(startIndex + i)
                    .replaceAll("\\s+[А-Я][а-я]+\\s+[А-Я]\\.[А-Я]\\.", "")
                    .trim()
                    .replaceAll("\\s{2,}", " ");
            schedule.append("\ud83d\udd39 `").append(line).append("`\n");
        }
        return schedule.toString();
    }

    private LocalDate parseStartDate(String dateData) {
        String dateString = dateData.split(" ")[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        log.info("Parsing start date from data: {}", dateString);
        return LocalDate.parse(dateString, formatter);
    }

    public long returnCountUsers() {
        long count = userDao.getCountUsers();
        log.info("Total number of users: {}", count);
        return count;
    }
}