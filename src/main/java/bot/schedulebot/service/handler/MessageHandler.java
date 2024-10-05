package bot.schedulebot.service.handler;

import bot.schedulebot.service.bot.MessageService;
import bot.schedulebot.telegram.Bot;
import bot.schedulebot.util.KeyBoardUtils;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageHandler {

    private final MessageService messageService;

    public BotApiMethod<?> answer(Message message) {
        log.info("Unknown message received from user ID \"{}\": \"{}\"", message.getFrom().getId(), message.getText());
        Faker faker = new Faker(new Locale("ru"));
        return messageService.executeMessage(faker.lorem().sentence(), message.getFrom().getId(), KeyBoardUtils.hideMethod());
    }
}