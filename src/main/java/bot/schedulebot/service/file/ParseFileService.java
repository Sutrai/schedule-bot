package bot.schedulebot.service.file;

import bot.schedulebot.domain.constant.ConstantsBot;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ParseFileService {

    private static final String FILE_PATH_PREFIX = ConstantsBot.FILE_DIRECTORY;

    public static List<String> parseExcel(String xls, Integer columnIndex) {
        List<String> subjects = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_PATH_PREFIX + xls);
             Workbook workbook = new HSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 8; i < 28; i++) {
                subjects.add(getCellValue(sheet.getRow(i), columnIndex));
            }
            subjects.add(getCellValue(sheet.getRow(4), columnIndex));

            String headerValue = getCellValue(sheet.getRow(3), 0);
            if (!headerValue.isEmpty()) {
                subjects.add(headerValue);
            }

        } catch (IOException e) {
            log.error("Error reading Excel file", e);
        }
        return subjects;
    }

    public static String getGroup(String xls, Integer groupId){
        if (groupId == null) return "-";

        try (FileInputStream fis = new FileInputStream(FILE_PATH_PREFIX + xls);
             Workbook workbook = new HSSFWorkbook(fis)) {

            return getCellValue(workbook.getSheetAt(0).getRow(4), groupId).trim();
        } catch (IOException e) {
            log.error("Error reading Excel file", e);
            return "-";
        }
    }

    public static String getTime(String xls) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH_PREFIX + xls);
             Workbook workbook = new HSSFWorkbook(fis)) {

            return getCellValue(workbook.getSheetAt(0).getRow(3), 0).trim();
        } catch (IOException e) {
            log.error("Error reading Excel file", e);
            return "-";
        }
    }

    private static String getCellValue(Row row, int columnIndex) {
        if (row == null) return "пропуск";
        Cell cell = row.getCell(columnIndex);
        if (cell == null) return "пропуск";

        if (cell.getCellType() == CellType.STRING) {
            String cellValue = cell.getStringCellValue().trim();
            return cellValue.isEmpty() ? "пропуск" : cellValue;
        }
        return "пропуск";
    }
}