package bot.schedulebot.service.mailing;

import bot.schedulebot.dao.impl.UserDaoService;
import bot.schedulebot.domain.constant.ConstantsBot;
import bot.schedulebot.domain.dto.user.User;
import bot.schedulebot.service.bot.MessageService;
import bot.schedulebot.telegram.Bot;
import bot.schedulebot.util.KeyBoardUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailingList {

    private final UserDaoService userDaoService;
    private final MessageService messageService;

    public BotApiMethod<?> sendBroadcastMessage(Bot bot) {
        List<User> allUsers = userDaoService.getAllUsers();
        int successfulMessagesCount = 0;

        for (User user : allUsers) {
            try {
                BotApiMethod<?> response = messageService.executeMessage(ConstantsBot.NEW_SCHEDULE_MESSAGE, user.getTelegramId(), KeyBoardUtils.implementationOfCheckButton());
                bot.execute(response);
                successfulMessagesCount++;
            } catch (Exception e) {
                log.error("Error occurred while sending message to user with chat_id: {}", user.getTelegramId(), e);
            }
        }

        log.info("Message delivered to {} users", successfulMessagesCount);
        return null;
    }
}