package bot.schedulebot.service.bot;

import bot.schedulebot.service.handler.CallbackQueryHandler;
import bot.schedulebot.service.handler.CommandHandler;
import bot.schedulebot.service.handler.DocumentHandler;
import bot.schedulebot.service.handler.MessageHandler;
import bot.schedulebot.telegram.Bot;
import bot.schedulebot.telegram.TelegramProperties;
import bot.schedulebot.util.KeyBoardUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateDispatcher {
    private final CommandHandler commandHandler;
    private final MessageHandler messageHandler;
    private final DocumentHandler documentHandler;
    private final CallbackQueryHandler queryHandler;
    private final TelegramProperties telegramProperties;
    private final MessageService messageService;

    public BotApiMethod<?> distribute(Update update, Bot bot) {
        if (update.hasCallbackQuery()) {
            return queryHandler.answer(update.getCallbackQuery(), bot);
        }
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                String text = message.getText();
                if (text.charAt(0) == '/') {
                    return commandHandler.answer(update.getMessage(), bot);
                }
                return messageHandler.answer(update.getMessage());
            }

        }
        if (update.getMessage().hasDocument()) {
            Document document = update.getMessage().getDocument();
            String fileName = document.getFileName();
            if (fileName != null && fileName.endsWith(".xls")) {
                if (update.getMessage().getFrom().getId() != telegramProperties.getAdminId()){
                    return messageService.executeMessage("Лучше звоните Солу", update.getMessage().getFrom().getId(), KeyBoardUtils.hideMethod());
                }
                return documentHandler.answer(document, bot);
            } else {
                log.warn("Unsupported document type: {" + fileName + "}");
                return null;
            }
        }
        return null;
    }

}
