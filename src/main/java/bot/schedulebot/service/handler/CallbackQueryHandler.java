package bot.schedulebot.service.handler;

import bot.schedulebot.dao.UserDao;
import bot.schedulebot.data.ButtonData;
import bot.schedulebot.domain.constant.ConstantsBot;
import bot.schedulebot.domain.dto.user.User;
import bot.schedulebot.service.bot.MessageService;
import bot.schedulebot.service.bot.TelegramBotService;
import bot.schedulebot.service.file.ParseFileService;
import bot.schedulebot.telegram.Bot;
import bot.schedulebot.util.KeyBoardUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackQueryHandler {

    private final TelegramBotService telegramBotService;
    private final MessageService messageService;
    private final UserDao userDao;

    public BotApiMethod<?> answer(CallbackQuery query, Bot bot) {
        long userId = query.getFrom().getId();
        int messageId = query.getMessage().getMessageId();

        log.info("Received callback query from userId: {}, messageId: {}", userId, messageId);

        if (ConstantsBot.groupMap.containsKey(query.getData())) {
            return handleGroupSelection(query, userId, messageId);
        }

        return switch (ButtonData.valueOf(query.getData())) {

            case SCHEDULE_BUTTON -> handleScheduleButton(query, userId, messageId);
            case BACK_BUTTON -> handleBackButton(userId, messageId);
            case MONDAY -> handleDaySchedule(0, userId, messageId);
            case TUESDAY -> handleDaySchedule(4, userId, messageId);
            case WEDNESDAY -> handleDaySchedule(8, userId, messageId);
            case THURSDAY -> handleDaySchedule(12, userId, messageId);
            case FRIDAY -> handleDaySchedule(16, userId, messageId);
            case CHECK -> handleGroupSelection(query, userId, messageId);
            case HIDE_METHOD -> messageService.deleteMessage(userId, messageId);
            case FIRST_SCHEDULE -> handleFileSelection(userId, messageId, "1.xls");
            case SECOND_SCHEDULE -> handleFileSelection(userId, messageId, "2.xls");
            case CHANGE_GROUP -> handleChangeGroup(userId, messageId);

        };
    }

    public BotApiMethod<?> handleChangeGroup(long userId, int messageId) {
        log.info("User {} is changing group.", userId);
        User user = userDao.findByUserId(userId);
        String groupInfo = ParseFileService.getGroup("1.xls", user.getGroupId());
        String text = String.format("*Текущая группа:*%n`%s`", groupInfo);
        return messageService.executeEditMessageText(text, userId, messageId, KeyBoardUtils.choiceGroup());
    }

    private BotApiMethod<?> handleGroupSelection(CallbackQuery query, long userId, int messageId) {
        log.info("User {} selected group: {}", userId, query.getData());
        userDao.setGroupId(userId, ConstantsBot.groupMap.get(query.getData()));
        return messageService.executeEditMessageText(ConstantsBot.CHOICE_SCHEDULE_MENU, userId, messageId, KeyBoardUtils.implementationOfCreateChoiceScheduleMenu());
    }

    private BotApiMethod<?> handleScheduleButton(CallbackQuery query, long userId, int messageId) {
        log.info("User {} pressed schedule button.", userId);
        userDao.insertNewUser(createUser(query));
        return messageService.executeEditMessageText(ConstantsBot.CHOICE_SCHEDULE_MENU, userId, messageId, KeyBoardUtils.implementationOfCreateChoiceScheduleMenu());
    }

    private BotApiMethod<?> handleFileSelection(long userId, int messageId, String fileName) {
        log.info("User {} is selecting file: {}", userId, fileName);
        userDao.setSelectedFile(userId, fileName);
        return telegramBotService.handleScheduleSelection(userId, messageId);
    }

    private BotApiMethod<?> handleBackButton(long userId, int messageId) {
        log.info("User {} pressed back button.", userId);
        return messageService.executeEditMessageText(ConstantsBot.CHOICE_SCHEDULE_MENU, userId, messageId, KeyBoardUtils.implementationOfCreateChoiceScheduleMenu());
    }

    private User createUser(CallbackQuery query) {
        return User.builder()
                .username(query.getFrom().getUserName())
                .telegramId(query.getFrom().getId())
                .build();
    }

    private BotApiMethod<?> handleDaySchedule(int offset, long userId, int messageId) {
        log.info("User {} is requesting schedule for day with offset: {}", userId, offset);
        return telegramBotService.handleDaySchedule(offset, userId, messageId);
    }
}