package bot.schedulebot.telegram;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class TelegramProperties {

    @Value("${bot.name}")
    private String name;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.url}")
    private String url;
    @Value("${bot.admin.ids}")
    private int adminId;
}