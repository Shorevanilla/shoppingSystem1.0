package org.example;

import java.util.Scanner;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User {
    String ID;
    String name;
    String password;
    int userLevel;
    String email;
    String TlNumber;
    float consumed;
    boolean ifLocked;

    // 差一个读取Excel获取id
    void register() {

        boolean ifquit = false;
        System.out.println("*********用户注册*********");
        // 用户名长度不少于5个字符；密码长度大于8个字符，必须是大小写字母、数字和标点符号的组合。
        Scanner scan = new Scanner(System.in);

        String tampleName = "";
        while (true) {
            System.out.println("请输入用户名，长度不少于5个字符，输入 'quit' 退出：");
            tampleName = scan.next();

            if (tampleName.equals("quit")) {
                break; // 退出循环
            }

            // 调用 ExceptionClass 中的方法来验证用户名是否重复
            if (!ExceptionClass.isUsernameUnique(tampleName)) {
                System.out.println("用户名已存在，请输入不同的用户名。");
                continue; // 继续下一次循环
            }

            try {
                if (ExceptionClass.validateUsername(tampleName)) {
                    break; // 用户名验证通过，退出循环
                }
            } catch (ExceptionClass.UsernameValidationException e) {
                System.out.println("用户名验证失败: " + e.getMessage());
            }
        }
        System.out.println("输入电话号码：");
        String tampleTlNum = scan.next();
        String tampleEmail = "";
        if (tampleTlNum != "quit") {
            System.out.println("输入电子邮箱：");
            tampleEmail = scan.next();
            if (tampleEmail == "quit")
                ifquit = true;
        } else
            ifquit = true;

        if (!ifquit) {
            String tPassword = "";
            System.out.println("输入密码,长度大于8个字符，必须是大小写字母、数字和标点符号的组合：");
            while (true) {

                tPassword = scan.next();

                if (tPassword.equals("quit")) {
                    break; // 退出循环
                }
                try {
                    try {
                        if (ExceptionClass.validatePassword(tPassword)) {
                            System.out.println("请再次输入密码：");
                            if (ExceptionClass.validateMatchingInputs(scan.next(), tPassword)) {
                                ifquit = false;
                                break; // 用户名验证通过，退出循环

                            }
                        }
                    } catch (ExceptionClass.PasswordValidationException e) {
                        System.out.println("密码不符合规范: " + e.getMessage());
                    }
                } catch (ExceptionClass.MatchingInputsValidationException e) {
                    System.out.println(e.getMessage());
                }

            }
            if (!ifquit) {
                try (Workbook workbook = WorkbookFactory.create(new FileInputStream("/workspace/shoppingSystem1.0/User.xlsx"))) {
                    Sheet sheet = workbook.getSheet("User");

                    // 找到下一个空行
                    int nextRowNum = findNextEmptyRow(sheet);

                    if (nextRowNum != -1) {
                        Row newRow = sheet.createRow(nextRowNum);

                        // 设置单元格的值，根据列的顺序
                        newRow.createCell(Excelor.ID).setCellValue(nextRowNum); // ID，假设 ID 是自动生成的
                        newRow.createCell(Excelor.Name).setCellValue(tampleName); // 用户名
                        newRow.createCell(Excelor.Password).setCellValue(tPassword); // 密码，这里没有加密，请根据实际需求处理
                        newRow.createCell(Excelor.userLevel).setCellValue("铜牌客户");
                        newRow.createCell(Excelor.email).setCellValue(tampleEmail);
                        newRow.createCell(Excelor.TlNumber).setCellValue(tampleTlNum);
                        newRow.createCell(Excelor.consumed).setCellValue(0);
                        newRow.createCell(Excelor.ifLocked).setCellValue(0);

                        LocalDateTime currentTime = LocalDateTime.now();
                        // 定义日期时间格式
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M.d-H:mm");
                        // 格式化当前时间为字符串
                        String formattedTime = currentTime.format(formatter);

                        newRow.createCell(8).setCellValue(formattedTime);

                        try (FileOutputStream fileOut = new FileOutputStream("/workspace/shoppingSystem1.0/User.xlsx")) {
                            workbook.write(fileOut);
                        }
                        System.out.println("用户注册成功，数据已写入Excel");
                    } else {
                        System.out.println("用户注册失败，无法找到空行来写入用户信息");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        scan.close();
    }
    
    private int findNextEmptyRow(Sheet sheet) {
        int startRowNum = sheet.getLastRowNum() + 1;
        for (int rowNum = startRowNum; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                return rowNum;
            }
        }
        return startRowNum;
    }
    
}

