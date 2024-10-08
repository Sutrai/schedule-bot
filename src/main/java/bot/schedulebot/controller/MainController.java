package bot.schedulebot.controller;

import bot.schedulebot.telegram.Bot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final Bot bot;

    @PostMapping("/webhook")
    public BotApiMethod<?> updateListener(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }
}