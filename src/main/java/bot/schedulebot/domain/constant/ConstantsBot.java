package bot.schedulebot.domain.constant;

import bot.schedulebot.service.file.ParseFileService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConstantsBot {

    public static final String START_MENU_TEXT = """
            ➖➖➖➖➖➖➖➖➖➖
                 ✦ *БОТ С РАСПИСАНИЕМ* ✦
            ➖➖➖➖➖➖➖➖➖➖
            `%d users`
            """;

    public static final String SCHEDULE_MENU_FIRST = "*Расписание %s*%n%n`Текущая группа: %s`%n";

    public static final String CHOICE_SCHEDULE_MENU = String.format("*\uD83D\uDCC5 Доступные расписания*%n%n*1.* - `%s`%n*2.* - `%s`",
            ParseFileService.getTime("1.xls"),
            ParseFileService.getTime("2.xls"));

    public static final String SUCCESSFUL_DOWNLOAD = "*Расписание успешно загружено*";
    public static final String FIRST_AND_SCHEDULE_TEXT = "*Выбор группы*";
    public static final String NEW_SCHEDULE_MESSAGE = "✉\uFE0F Доступно новое расписание";

    public static final String FILE_DIRECTORY = "/var/rkn/";
//    public static final String FILE_DIRECTORY = "/home/oous/rasp";

    public static final Map<String, Integer> groupMap = new HashMap<>() {{
        put("ТД-13", 2);
        put("ТГ-13/ТГ-13А", 3);
        put("ТГП-13/ТГП-13А", 4);
        put("Ю-13/Ю-13А", 5);
        put("Р-13", 6);
        put("ИС-14/ИС-14А", 7);
        put("К-23", 10);
        put("ТГ-23/ТГ-23А", 11);
        put("Ю-23/Ю-23А", 12);
        put("Ю-12", 13);
        put("Р-24", 14);
        put("ИС-24", 17);
        put("ИС-24А/ИС-13", 18);
        put("К-33/К-22", 19);
        put("Г-34/Г-34А", 20);
        put("Ю-33А/Ю-22А", 21);
        put("Ю-33/Ю-22", 22);
        put("Р-34/Р-23", 25);
        put("ИС-34/ИС-23", 26);
        put("ПК-34/ПК-34А/ПК-23", 27);
        put("ИС-44/ИС-33", 28);
    }};
}