package org.example;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;



public class ExceptionClass {
    static boolean ifContinue() {
        System.out.println("输入back返回上一级，否则继续");
        Scanner scan = new Scanner(System.in);
        String tag = scan.next();
        scan.close();
        return !tag.equals("back");
    }
   
    public static boolean validateUsername(String username) throws UsernameValidationException {
        if (username.length() < 5) {
            throw new UsernameValidationException("用户名长度不能少于5个字符");
        }
        return true;
    }

    public static boolean isUsernameUnique(String username) {
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(Excelor.FilePath))) {
            Sheet sheet = workbook.getSheet("User");

            for (Row row : sheet) {
                Cell usernameCell = row.getCell(Excelor.Name); // 参数为用户名所在列
                if (usernameCell != null && usernameCell.getStringCellValue().equals(username)) {
                    return false; // 发现重复用户名
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true; // 未发现重复用户名
    }

    public static boolean isTlNOUnique(String tlNumber) {
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(Excelor.FilePath))) {
            Sheet sheet = workbook.getSheet("User");

            for (Row row : sheet) {
                Cell usertlNumberCell = row.getCell(Excelor.TlNumber); // 参数为电话号码所在列
                if (usertlNumberCell != null && usertlNumberCell.getStringCellValue().equals(tlNumber)) {
                    return false; // 发现存在电话号码
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true; // 未发现电话号码
    }
    public static boolean isDataUnique(String FilePath,String sheetName,String data,int serchRow) {
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(FilePath))) {
            Sheet sheet = workbook.getSheet(sheetName);

            for (Row row : sheet) {
                Cell Cell = row.getCell(serchRow); // 参数为所在列
                if (Cell != null && Excelor.getValueAsString(Cell).equals(data)) {
                    return false; // 发现
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true; // 未发现
    }

    public static boolean isPasswordCorrect(String input, String password, int loginType) {
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(Excelor.FilePath))) {
            Sheet sheet = workbook.getSheet("User");
            
            int rowNum = findRowByInput(sheet, input, loginType);
            if (rowNum != -1) {
                Row row = sheet.getRow(rowNum);
                Cell userPasswordCell = row.getCell(Excelor.Password); // 假设密码在第3列
                
                if (userPasswordCell != null && userPasswordCell.getStringCellValue().equals(password)) {
                    return true; // 验证成功
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // 验证失败
    }
    public static boolean isPasswordCorrect(String filePath,String sheetName,String name, String password) {
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))) {
            Sheet sheet = workbook.getSheet(sheetName);
            
            int rowNum = findRowByInput(sheet, name, Excelor.Name);
            if (rowNum != -1) {
                Row row = sheet.getRow(rowNum);
                Cell userPasswordCell = row.getCell(Excelor.Password); // 假设密码在第3列
                
                if (userPasswordCell != null && userPasswordCell.getStringCellValue().equals(password)) {
                    return true; // 验证成功
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // 验证失败
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
  






    
    public static boolean validatePassword(String password) throws PasswordValidationException {
        // Modified pattern for at least two of (letter, digit, symbol) and at least 8 characters
        String pattern = "^(?=(?:.*[a-zA-Z]){0,1})(?=(?:.*\\d){0,1})(?=(?:.*[@$!%*?&#]){0,1})([A-Za-z\\d@$!%*?&#]{8,})$";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(password);
        
        if (!matcher.matches()) {
            if (password.length() < 8) {
                throw new PasswordValidationException("密码长度至少为8位");
            } else if (!matcher.matches()) {
                throw new PasswordValidationException("密码必须包含字母、数字和符号的其中两个");
            } else if (!password.matches("[A-Za-z\\d@$!%*?&#]+")) {
                throw new PasswordValidationException("密码包含了无效的字符");
            }
        }
        
        return true;
    }
    
    
    

    public static boolean validateMatchingInputs(String input1, String input2) throws MatchingInputsValidationException {
        if (!input1.equals(input2)) {
            throw new MatchingInputsValidationException("两次输入不相同");
        }
        return true;
    }

    // 自定义异常类
    static class UsernameValidationException extends Exception {
        public UsernameValidationException(String message) {
            super(message);
        }
    }

    static class PasswordValidationException extends Exception {
        public PasswordValidationException(String message) {
            super(message);
        }
    }
    
    static class MatchingInputsValidationException extends Exception {
        public MatchingInputsValidationException(String message) {
            super(message);
        }
    }
}
