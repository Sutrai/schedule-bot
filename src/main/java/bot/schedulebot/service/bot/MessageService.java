package bot.schedulebot.service.bot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
@Slf4j
public class MessageService {

    public BotApiMethod<?> executeEditMessageText(String text, long userId, long messageId, InlineKeyboardMarkup button) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(userId));
        message.setText(text);
        message.disableWebPagePreview();
        message.setReplyMarkup(button);
        message.setMessageId((int) messageId);
        message.setParseMode(ParseMode.MARKDOWN);
        try {
            return message;
        } catch (Exception e) {
            message.setText(text + "\u200B");
            return message;
        }
    }

    public BotApiMethod<?> executeMessage(String text, long chatId, InlineKeyboardMarkup markup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.disableWebPagePreview();
        message.setParseMode(ParseMode.MARKDOWN);
        message.setReplyMarkup(markup);
        return message;
    }

    public BotApiMethod<?> deleteMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(chatId));
        deleteMessage.setMessageId(messageId);
        return deleteMessage;
    }
}