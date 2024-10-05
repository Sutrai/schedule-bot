package bot.schedulebot.util;

import bot.schedulebot.data.ButtonData;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyBoardUtils {

    private static InlineKeyboardButton createButton(String text, Object callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(String.valueOf(callbackData));
        return button;
    }

    private static InlineKeyboardMarkup createMarkup(List<List<InlineKeyboardButton>> rows) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    public static InlineKeyboardMarkup implementationOfCreateMainMenuMarkup() {
        InlineKeyboardButton startButton = createButton(">>Расписание<<", ButtonData.SCHEDULE_BUTTON);
        List<InlineKeyboardButton> rowInLine1 = List.of(startButton);
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInLine1, rowInLine2);
        return createMarkup(rowsInLine);
    }

    public static InlineKeyboardMarkup implementationOfMondayMarkup() {
        InlineKeyboardButton mondayButton = createButton("понедельник", ButtonData.MONDAY);
        InlineKeyboardButton tuesdayButton = createButton("вторник", ButtonData.TUESDAY);
        InlineKeyboardButton wednesdayButton = createButton("среда", ButtonData.WEDNESDAY);
        InlineKeyboardButton thursdayButton = createButton("четверг", ButtonData.THURSDAY);
        InlineKeyboardButton fridayButton = createButton("пятница", ButtonData.FRIDAY);
        InlineKeyboardButton back = createButton("◀\uFE0F назад", ButtonData.BACK_BUTTON);

        List<InlineKeyboardButton> rowInLine1 = List.of(mondayButton, tuesdayButton);
        List<InlineKeyboardButton> rowInLine2 = List.of(wednesdayButton, thursdayButton);
        List<InlineKeyboardButton> rowInLine3 = List.of(fridayButton);
        List<InlineKeyboardButton> rowInLine4 = List.of(back);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInLine1, rowInLine2, rowInLine3, rowInLine4);

        return createMarkup(rowsInLine);
    }

    public static InlineKeyboardMarkup implementationOfCreateChoiceScheduleMenu() {
        InlineKeyboardButton first = createButton("1", ButtonData.FIRST_SCHEDULE);
        InlineKeyboardButton second = createButton("2", ButtonData.SECOND_SCHEDULE);
        InlineKeyboardButton change_group = createButton("\ud83d\udd39 изменить группу", ButtonData.CHANGE_GROUP);
        List<InlineKeyboardButton> rowInLine1 = List.of(first, second);
        List<InlineKeyboardButton> rowInLine2 = List.of(change_group);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInLine1, rowInLine2);
        return createMarkup(rowsInLine);
    }

    public static InlineKeyboardMarkup implementationOfCheckButton() {
        InlineKeyboardButton first = createButton(">>посмотреть<<", ButtonData.CHECK);
        List<InlineKeyboardButton> rowInLine1 = List.of(first);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInLine1);
        return createMarkup(rowsInLine);
    }

    public static InlineKeyboardMarkup choiceGroup() {
        String buttonNames = "ТД-13,ТГ-13/ТГ-13А,ТГП-13/ТГП-13А,Ю-13/Ю-13А,Р-13,ИС-14/ИС-14А,К-23,ТГ-23/ТГ-23А,Ю-23/Ю-23А,Ю-12,Р-24,ИС-24,ИС-24А/ИС-13,К-33/К-22,Г-34/Г-34А,Ю-33А/Ю-22А,Ю-33/Ю-22,Р-34/Р-23,ИС-34/ИС-23,ПК-34/ПК-34А/ПК-23,ИС-44/ИС-33";
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        for (String name : buttonNames.split(",")) {
            buttons.add(createButton(name, name));
        }
        buttons.add(createButton("◀\uFE0F назад", ButtonData.BACK_BUTTON));

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i += 3) {
            int end = Math.min(i + 3, buttons.size());
            rowsInLine.add(buttons.subList(i, end));
        }

        return createMarkup(rowsInLine);
    }

    public static InlineKeyboardMarkup hideMethod() {
        InlineKeyboardButton backButton = createButton("скрыть", ButtonData.HIDE_METHOD);
        List<InlineKeyboardButton> rowInLine = List.of(backButton);
        return createMarkup(List.of(rowInLine));
    }
}