package bot.schedulebot.service.file;

import bot.schedulebot.domain.constant.ConstantsBot;
import bot.schedulebot.telegram.Bot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@Service
public class DownloadFileService {

    public void downloadFile(Document document, Bot bot) {
        log.info("Downloading file: {}", document.getFileName());
        try {
            GetFile getFileRequest = new GetFile();
            getFileRequest.setFileId(document.getFileId());
            File file = bot.execute(getFileRequest);

            String filePath = file.getFilePath();
            InputStream is = bot.downloadFileAsStream(filePath);

            String destinationDir = ConstantsBot.FILE_DIRECTORY;

            String destinationFileName = document.getFileName();
            String destinationFilePath = destinationDir + destinationFileName;

            try (OutputStream os = new FileOutputStream(destinationFilePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                log.info("File downloaded successfully: {}", destinationFilePath);
            }
        } catch (TelegramApiException | java.io.IOException e) {
            log.error("Error downloading file: {}", e.getMessage(), e);
        }
    }

    public void xlsxToXls(Document document) {
        //TODO
    }
}