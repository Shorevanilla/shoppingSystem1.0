package org.example;

import java.util.UUID;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Cell;

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
static void searchSingleCommodity(String serchData,int DataType){
  switch(DataType){
    case Excelor.Serial_NO:
    
  }
    
}
public static void showSingleCommodity(String filePath, String sheetName, int row) {
    try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))) {
        Sheet sheet = workbook.getSheet(sheetName);

        Row targetRow = sheet.getRow(row ); // 行号从0开始，索引从0开始
        if (targetRow != null) {
            Cell cellSerialNO = targetRow.getCell(Excelor.Serial_NO);
            Cell cellName = targetRow.getCell(Excelor.Name);
            Cell cellManufacturer = targetRow.getCell(Excelor.Manufacturer);
            Cell cellMnDate = targetRow.getCell(Excelor.MnDate);
            Cell cellType = targetRow.getCell(Excelor.Type);
            Cell cellPrimeCost = targetRow.getCell(Excelor.Prime_Cost);
            Cell cellPrice = targetRow.getCell(Excelor.Price);
            Cell cellAmount = targetRow.getCell(Excelor.Amount);

            System.out.println("商品编号: " + cellSerialNO.getStringCellValue());
            System.out.println("商品名称: " + cellName.getStringCellValue());
            System.out.println("生产厂家: " + cellManufacturer.getStringCellValue());
            System.out.println("生产日期: " + cellMnDate.getStringCellValue());
            System.out.println("产品型号: " + cellType.getStringCellValue());
            System.out.println("进货价: " + cellPrimeCost.getNumericCellValue());
            System.out.println("零售价格: " + cellPrice.getNumericCellValue());
            System.out.println("数量: " + cellAmount.getNumericCellValue()+"\n");
        } else {
            System.out.println("指定行不存在或为空。");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


}

