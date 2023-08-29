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
public static void showSingleUser(String filePath, String sheetName, int row) {
    try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))) {
        Sheet sheet = workbook.getSheet(sheetName);

        Row targetRow = sheet.getRow(row); // 行号从0开始，索引从0开始
        if (targetRow != null) {
            Cell cellID = targetRow.getCell(Excelor.ID);
            Cell cellName = targetRow.getCell(Excelor.Name);
            //Cell cellPassword = targetRow.getCell(Excelor.Password);
            Cell cellUserLevel = targetRow.getCell(Excelor.userLevel);
            Cell cellEmail = targetRow.getCell(Excelor.email);
            Cell cellTlNumber = targetRow.getCell(Excelor.TlNumber);
            Cell cellConsumed = targetRow.getCell(Excelor.consumed);
           // Cell cellIfLocked = targetRow.getCell(Excelor.ifLocked);
 
           int userID = (int) cellID.getNumericCellValue();
            System.out.println("用户ID: " + userID);
            System.out.println("用户名: " + cellName.getStringCellValue());
            //System.out.println("密码: " + cellPassword.getStringCellValue());
            System.out.println("用户等级: " + cellUserLevel.getStringCellValue());
            System.out.println("邮箱: " + cellEmail.getStringCellValue());
            System.out.println("电话号码: " + cellTlNumber.getStringCellValue());
            System.out.println("积累消费金额: " + cellConsumed.getNumericCellValue()+"\n");
            //System.out.println("是否锁定: " + cellIfLocked.getStringCellValue() + "\n");

        } else {
            System.out.println("指定行不存在或为空。");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
public static void queryUser(String filePath, String sheetName, String searchTerm) {
    try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))) {
        Sheet sheet = workbook.getSheet(sheetName);

        System.out.println("************ 查询客户结果如下 ************");

        // Starting from the second row (index 1)
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cellID = row.getCell(Excelor.ID);
                Cell cellName = row.getCell(Excelor.Name);

                if (cellID != null && cellName != null) {
                    int userID = (int) cellID.getNumericCellValue();
                    String username = cellName.getStringCellValue();

                    if (String.valueOf(userID).contains(searchTerm) || username.toLowerCase().contains(searchTerm.toLowerCase())) {
                        System.out.println("用户ID: " + userID);
                        System.out.println("用户名: " + username);
                        System.out.println("-----------------------------");
                    }
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static void showAllCommodityIF(){
    System.out.println("*******************所有商品信息展示*******************");

    int lastRow= Excelor.findNextEmptyRow(Excelor.commodityPath,Excelor.sheetName_commodity );
    for(int i=1;i<lastRow;i++)
    {Manager.showSingleCommodity(Excelor.commodityPath,Excelor.sheetName_commodity, i);
        
    }
  }

  public static void  showAllUserDetail(){
    System.out.println("*******************所有用户信息如下*******************");

    int lastRow= Excelor.findNextEmptyRow(Excelor.FilePath,Excelor.sheetName_user );
    for(int i=1;i<lastRow;i++)
    {
        Manager.showSingleUser(Excelor.FilePath,Excelor.sheetName_user, i);
        
    }
  }
  public static void showAllUserIF() {
    System.out.println("*******************所有用户如下*******************");

    try (Workbook workbook = WorkbookFactory.create(new FileInputStream(Excelor.FilePath))) {
        Sheet sheet = workbook.getSheet(Excelor.sheetName_user);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cellID = row.getCell(Excelor.ID);
                Cell cellName = row.getCell(Excelor.Name);

                if (cellID != null && cellName != null) {
                    int userID = (int) cellID.getNumericCellValue();
                    String username = cellName.getStringCellValue();

                    System.out.println("用户ID: " + userID);
                    System.out.println("用户名: " + username);
                    System.out.println("-----------------------------");
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
public static void searchUser(String filePath, String sheetName, String searchTerm) {
    try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))) {
        Sheet sheet = workbook.getSheet(sheetName);

        System.out.println("************ 查询客户结果如下 ************");

        boolean found = false; // Flag to track if any user is found

        // Starting from the second row (index 1)
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cellID = row.getCell(Excelor.ID);
                Cell cellName = row.getCell(Excelor.Name);

                if (cellID != null && cellName != null) {
                    int userID = (int) cellID.getNumericCellValue();
                    String username = cellName.getStringCellValue();

                    if (String.valueOf(userID).equals(searchTerm) || username.equals(searchTerm)) {
                        System.out.println("用户ID: " + userID);
                        System.out.println("用户名: " + username);
                        //System.out.println("密码: " + row.getCell(Excelor.Password).getStringCellValue());
                        System.out.println("用户级别: " + row.getCell(Excelor.userLevel).getStringCellValue());
                        System.out.println("电子邮件: " + row.getCell(Excelor.email).getStringCellValue());
                        System.out.println("联系电话: " + row.getCell(Excelor.TlNumber).getStringCellValue());
                        System.out.println("已消费金额: " + row.getCell(Excelor.consumed).getNumericCellValue());
                       // System.out.println("是否锁定: " + row.getCell(Excelor.ifLocked).getStringCellValue());
                        System.out.println("-----------------------------");

                        found = true;
                    }
                }
            }
        }

        if (!found) {
            System.out.println("未找到匹配的用户。");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}



}

