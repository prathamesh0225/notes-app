package utils;

import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    public static Object[][] getSheetData(String sheetName) {
        Object[][] data = null;

        try {

            FileInputStream file = new FileInputStream("testdata/data.xlsx");
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();

            data = new Object[rowCount][colCount];

            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= rowCount; i++) {

                for (int j = 0; j < colCount; j++) {

                    Cell cell = sheet.getRow(i).getCell(j,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    data[i - 1][j] = formatter.formatCellValue(cell);
                }
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}