package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Excelor {
    public static final int ID = 0;
    public static final int Name = 1;
    public static final int Password = 2;
    public static final int userLevel = 3;
    public static final int email = 4;
    public static final int TlNumber = 5;
    public static final int consumed = 6;
    public static final int ifLocked = 7;
    public static String FilePath = "/workspace/shoppingSystem1.0/User.xlsx";
    public static String sheetName_user = "User";
    public static String sheetName_manager = "Manager";
    public static String mangerPath="/workspace/shoppingSystem1.0/Manager.xlsx";
    
    public static String commodityPath="/workspace/shoppingSystem1.0/Commodity.xlsx";
    public static String sheetName_commodity = "Commodity";
    public static final int  Serial_NO=0;
    public static final int Manufacturer=2;
    public static final int MnDate=3;
    public static final int Type=4;
    public static final int Prime_Cost=5;
    public static final int Price=6;
    public static final int  Amount=7;

    public static void userExcelCreator() {
        String[] columnHeaders = { "ID", "Name", "Password", "User Level", "Email", "Tl Number", "Consumed",
                "If Locked", "Register time" };

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("User");

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
            }
            // Save the Excel file
            try (FileOutputStream fileOut = new FileOutputStream("User.xlsx")) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

public static void managerExcelCreator() {
    String[] columnHeaders = { "ID", "Name", "Password" };

    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Manager");

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
        }
        
        // Create admin user
        Row adminRow = sheet.createRow(1);
        adminRow.createCell(0).setCellValue(1); // ID
        adminRow.createCell(1).setCellValue("admin"); // Name
        adminRow.createCell(2).setCellValue("ynuinfo#777"); // Password
        
        // Save the Excel file
        try (FileOutputStream fileOut = new FileOutputStream("Manager.xlsx")) {
            workbook.write(fileOut);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static void commodityExcelCreator() {
    String[] columnHeaders = { "Serial NO", "Name", "Manufacturer", "MnDate", "Type", "Prime Cost", "Price", "Amount" };

    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Commodity");

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
        }
        
        // Create sample commodity
        Row commodityRow = sheet.createRow(1);
        commodityRow.createCell(0).setCellValue("123456"); // Serial NO
        commodityRow.createCell(1).setCellValue("Sample Commodity"); // Name
        commodityRow.createCell(2).setCellValue("Sample Manufacturer"); // Manufacturer
        commodityRow.createCell(3).setCellValue("2023-07-11"); // MnDate
        commodityRow.createCell(4).setCellValue("Sample Type"); // Type
        commodityRow.createCell(5).setCellValue(10.0); // Prime Cost
        commodityRow.createCell(6).setCellValue(15.0); // Price
        commodityRow.createCell(7).setCellValue(100); // Amount
        
        // Save the Excel file
        try (FileOutputStream fileOut = new FileOutputStream("Commodity.xlsx")) {
            workbook.write(fileOut);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
public static int findNextEmptyRow(String filePath, String sheetName) {
    try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))) {
        Sheet sheet = workbook.getSheet(sheetName);
        int lastRowNum = sheet.getLastRowNum();
        for (int rowNum = 0; rowNum <= lastRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                return rowNum;
            }
        }
        return lastRowNum + 1;
    } catch (IOException e) {
        e.printStackTrace();
    }
    return -1; // Return -1 if there's an error
}

public static int findRowByInput(Sheet sheet, String input, int loginType) {
    for (Row row : sheet) {
        Cell usertlNumberCell = row.getCell(loginType);
        if (usertlNumberCell != null && usertlNumberCell.getStringCellValue().equals(input)) {
            return row.getRowNum(); // 返回行号
        }
    }
    return -1; // 未找到
}



public static int findDataRow(String filePath, String sheetName, String data, int searchColumn) {
    try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))) {
        Sheet sheet = workbook.getSheet(sheetName);

        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row != null) {
                Cell cell = row.getCell(searchColumn);
                if (cell != null && Excelor.getValueAsString(cell).equals(data)) {
                    return rowNum + 1; // 返回行号（行号从1开始）
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return -1; // 未找到，返回-1
}



    public static String getDataFromExcel(String filePath, String sheetPath, int searchColumnIndex, String searchValue,
            int targetColumnIndex) {
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath))) {
            Sheet sheet = workbook.getSheet(sheetPath);

            for (Row row : sheet) {
                Cell cell = row.getCell(searchColumnIndex);
                if (cell != null) {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.equals(searchValue)) {
                        Cell targetCell = row.getCell(targetColumnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        return getValueAsString(targetCell);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getCellValueByRowColumn(String filePath, String sheetName, int rowNumber, int columnNumber) {
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))) {
            Sheet sheet = workbook.getSheet(sheetName);
            
            Row row = sheet.getRow(rowNumber - 1); // 行号从1开始，索引从0开始
            if (row != null) {
                Cell cell = row.getCell(columnNumber ); // 列号从0开始，索引从0开始
                if (cell != null) {
                    return getValueAsString(cell);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ""; // 未找到单元格或发生错误，返回空字符串
    }
    public static void updateDataInExcel(String filePath, String sheetName, int searchColumnIndex, String searchValue,
            int targetColumnIndex, String newValue) {
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath))) {
            Sheet sheet = workbook.getSheet(sheetName);

            for (Row row : sheet) {
                Cell cell = row.getCell(searchColumnIndex);
                if (cell != null && Excelor.getValueAsString(cell).equals(searchValue)) {
                    Cell targetCell = row.getCell(targetColumnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    targetCell.setCellValue(newValue);
                    break; // Assuming you want to update only the first match
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateDataInExcel(String filePath, String sheetName, int targetRow, int targetColumn, String newValue) {
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath))) {
            Sheet sheet = workbook.getSheet(sheetName);

            if (targetRow >= 1 && targetRow <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(targetRow - 1); // 行号从1开始，索引从0开始
                if (row != null) {
                    Cell targetCell = row.getCell(targetColumn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    targetCell.setCellValue(newValue);
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public static String getValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }

                double numericValue = cell.getNumericCellValue();
                if (numericValue == Math.floor(numericValue) && !Double.isInfinite(numericValue)) {
                    // Check if the numeric value is an integer
                    return Integer.toString((int) numericValue);
                } else {
                    return Double.toString(numericValue);
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

}
