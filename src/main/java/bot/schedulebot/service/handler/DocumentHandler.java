package bot.schedulebot.service.handler;

import bot.schedulebot.service.file.DownloadFileService;
import bot.schedulebot.telegram.Bot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Document;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentHandler {

    private final DownloadFileService downloadFileService;

    public BotApiMethod<?> answer(Document document, Bot bot) {
        downloadFileService.downloadFile(document, bot);
        return null; //TODO
    }
}
