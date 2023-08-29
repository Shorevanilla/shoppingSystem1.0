package org.example;

import java.util.UUID;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Manager {
    String name;
    String password;

    static String resetUserPassword(String filePath, int searchColumnIndex, String searchValue, 
    int targetColumnIndex) {
        String newPassword = UUID.randomUUID().toString() + "Aa!";
        Excelor.updateDataInExcel(filePath, Excelor.sheetName_user, searchColumnIndex, 
        searchValue, targetColumnIndex,newPassword);
        return newPassword;
    }

    static void resetSelfPassword(String filePath, int searchColumnIndex, String searchValue, 
    int targetColumnIndex,String newPassword) {

        Excelor.updateDataInExcel(filePath, Excelor.sheetName_manager, searchColumnIndex, searchValue,
                targetColumnIndex, newPassword);
    }

    static void displayUserInformation() {
    }

    static void displayAllUserInformation() {
    }

    public static void addCommodity(String filePath, String sheetName, String serialNO, String name,
                                String manufacturer, String mnDate, String type, float primeCost,
                                float price, int amount) {
    try (Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath))) {
        Sheet sheet = workbook.getSheet(sheetName);

        int nextRowNum = Excelor.findNextEmptyRow(filePath, sheetName);

        if (nextRowNum != -1) {
            Row newRow = sheet.createRow(nextRowNum);

            newRow.createCell(Excelor.Serial_NO).setCellValue(serialNO);
            newRow.createCell(Excelor.Name).setCellValue(name);
            newRow.createCell(Excelor.Manufacturer).setCellValue(manufacturer);
            newRow.createCell(Excelor.MnDate).setCellValue(mnDate);
            newRow.createCell(Excelor.Type).setCellValue(type);
            newRow.createCell(Excelor.Prime_Cost).setCellValue(primeCost);
            newRow.createCell(Excelor.Price).setCellValue(price);
            newRow.createCell(Excelor.Amount).setCellValue(amount);

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
 
}
static void modifyCommodityIF(int targetRowIndexint, int targetColumnIndex,String newValue) {
    Excelor.updateDataInExcel(Excelor.commodityPath,Excelor.sheetName_commodity,targetRowIndexint,targetColumnIndex,newValue);
}
}

