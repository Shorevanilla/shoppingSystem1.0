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
