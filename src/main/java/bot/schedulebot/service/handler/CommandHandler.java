package bot.schedulebot.service.handler;

import bot.schedulebot.data.CommandData;
import bot.schedulebot.domain.constant.ConstantsBot;
import bot.schedulebot.service.bot.MessageService;
import bot.schedulebot.service.bot.TelegramBotService;
import bot.schedulebot.service.mailing.MailingList;
import bot.schedulebot.telegram.Bot;
import bot.schedulebot.telegram.TelegramProperties;
import bot.schedulebot.util.KeyBoardUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandHandler {

    private final MessageService messageService;
    private final MailingList mailingList;
    private final TelegramBotService telegramBotService;
    private final TelegramProperties telegramProperties;

    public BotApiMethod<?> answer(Message message, Bot bot) {
        String command = message.getText().substring(1);
        CommandData commandData;

        log.info("Received command: {} from userId: {}", command, message.getFrom().getId());

        try {
            commandData = CommandData.valueOf(command);
        } catch (Exception e) {
            log.warn("Unsupported command was received: {}", command);
            return null; //TODO
        }

        switch (commandData) {
            case start -> {
                log.info("User {} started the bot.", message.getFrom().getId());
                return messageService.executeMessage(
                        String.format(ConstantsBot.START_MENU_TEXT, telegramBotService.returnCountUsers()),
                        message.getChatId(),
                        KeyBoardUtils.implementationOfCreateMainMenuMarkup()
                );
            }
            case check -> {
                log.info("user {} wrote the command check", message.getFrom().getId());
                // TODO
            }

            case send -> {
                if (isAdmin(message.getFrom().getId())) {
                    log.info("Admin {} sent a broadcast message.", message.getFrom().getId());
                    return mailingList.sendBroadcastMessage(bot);
                } else {
                    log.warn("User {} attempted to use admin command: send.", message.getFrom().getId());
                }
            }
            case restart -> {
                if (isAdmin(message.getFrom().getId())) {
                    log.info("Admin {} restarted the bot.", message.getFrom().getId());
                    return null; //TODO
                }
            }
            default -> {
                log.warn("Unhandled command: {}", commandData);
                throw new UnsupportedOperationException();
            }
        }
        return null;
    }

    private boolean isAdmin(Long userId) {
        return telegramProperties.getAdminId() == userId;
    }
}
