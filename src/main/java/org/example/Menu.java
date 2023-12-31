package org.example;

import java.util.Scanner;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Menu {
    // String input;
    
    boolean if_access = false;

    void userRegister() {

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
                try (Workbook workbook = WorkbookFactory
                        .create(new FileInputStream("/workspace/shoppingSystem1.0/User.xlsx"))) {
                    Sheet sheet = workbook.getSheet("User");

                    // 找到下一个空行
                    int nextRowNum = findNextEmptyRow(sheet);

                    if (nextRowNum != -1) {
                        Row newRow = sheet.createRow(nextRowNum);

                        // 设置单元格的值，根据列的顺序
                        newRow.createCell(Excelor.ID).setCellValue(String.valueOf(nextRowNum)); // ID，假设 ID 是自动生成的
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

                        try (FileOutputStream fileOut = new FileOutputStream(
                                "/workspace/shoppingSystem1.0/User.xlsx")) {
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

    void userLogIN() {

        String input = "";
        String input2 = "";
        Scanner scanner = new Scanner(System.in);

        double ifLocked;
        while (!input.equals("quit") && !if_access) {
            System.out.println("***********用户登入***********");
            System.out.println("请选择登录方式：");
            System.out.println("1. 电话号码登录");
            System.out.println("2. 用户名登录");
            input = scanner.next();

            if (input.equals("1")) {
                System.out.println("请输入电话号码：");
                input = scanner.next();
                if (!input.equals("quit")) {
                    if (!ExceptionClass.isTlNOUnique(input))
                        while (!input2.equals("quit")) {
                            String tt = Excelor.getDataFromExcel(Excelor.FilePath, "User", Excelor.TlNumber, input,
                                    Excelor.ifLocked);
                            ifLocked = Double.parseDouble(tt);

                            if (ifLocked >= 5) {
                                System.out.println("该账号已被锁定，请联系管理员解锁");
                                break;
                            }

                            System.out.println("请输入密码：");
                            input2 = scanner.next();
                            if (ExceptionClass.isPasswordCorrect(input, input2, Excelor.TlNumber)) {
                                if_access = true;
                                Excelor.updateDataInExcel(Excelor.FilePath, Excelor.sheetName_user, Excelor.TlNumber,
                                        input, Excelor.ifLocked,
                                        String.valueOf(0));
                                System.out.println("登入成功");
                                break;
                            } else if (!input2.equals("quit")) {
                                System.out.println("密码错误请，重新输入密码：");
                                ifLocked++;
                                Excelor.updateDataInExcel(Excelor.FilePath, Excelor.sheetName_user, Excelor.TlNumber,
                                        input, Excelor.ifLocked,
                                        String.valueOf(ifLocked));
                                if (ifLocked == 4)
                                    System.out.println("该账号已经连续输入错误四次，连续错误五次该账号将被锁定");
                            }
                        }
                    else {
                        System.out.println("输入账号错误");
                    }

                }

            } else if (input.equals("2")) {
                System.out.println("请输入用户名：");
                input = scanner.next();
                if (!input.equals("quit")) {
                    if (!ExceptionClass.isUsernameUnique(input))
                        while (!input2.equals("quit")) {
                            ifLocked = Double.parseDouble(
                                    Excelor.getDataFromExcel(Excelor.FilePath, "User", Excelor.Name, input,
                                            Excelor.ifLocked));
                            if (ifLocked >= 5) {
                                System.out.println("该账号已被锁定，请联系管理员解锁");
                                break;
                            }

                            System.out.println("请输入密码：");
                            input2 = scanner.next();
                            if (ExceptionClass.isPasswordCorrect(input, input2, Excelor.Name)) {
                                if_access = true;
                                Excelor.updateDataInExcel(Excelor.FilePath, Excelor.sheetName_user, Excelor.Name, input,
                                        Excelor.ifLocked,
                                        String.valueOf(0));
                                System.out.print("登入成功");
                                break;
                            } else if (!input2.equals("quit")) {
                                System.out.println("密码错误请，重新输入密码：");
                                ifLocked++;
                                Excelor.updateDataInExcel(Excelor.FilePath, Excelor.sheetName_user, Excelor.Name, input,
                                        Excelor.ifLocked,
                                        String.valueOf(ifLocked));
                                if (ifLocked == 4)
                                    System.out.println("该账号已经连续输入错误四次，连续错误五次该账号将被锁定");
                            }
                        }
                    else {
                        System.out.println("输入账号错误");
                    }
                }
            } else {
                if (input.equals("quit"))
                    break;
                System.out.println("无效选择，请重新输入。");
                continue;
            }

        }
        scanner.close();
    }

    void managerLogIN() {
        String input="";
        Scanner scanner = new Scanner(System.in);
    while(true){
        System.out.println("***********管理员登入***********");

        System.out.println("请输入用户名：");
        input = scanner.next();
        if(input.equals("quit")) break;
        if (!ExceptionClass.isDataUnique(Excelor.mangerPath,Excelor.sheetName_manager,input,Excelor.Name)) {
           while(true){
            System.out.println("请输入密码：");
            String password = scanner.next();
            if(password.equals("quit")) break;
            if (ExceptionClass.isPasswordCorrect(input, password, Excelor.Name)) {
                if_access = true;
                System.out.println("管理员登入成功");
                scanner.close();
                return;
            } else {
                System.out.println("密码错误，请重新尝试");
            }
        }
        } else {
            System.out.println("用户名不存在");
        }
    }
    scanner.close();
    }
   
   void logOut(){
    System.out.println("已经退出登入");
    if_access=false;
   } 
    
    void resetManagerPassword() {
        boolean ifquit = false;
        String ID = "";
        String newPassword = "";
        String input = "";
        Scanner scan = new Scanner(System.in);

        while (!ID.equals("quit")) {// 管理员密码修改
            System.out.println("***********管理员密码修改***********");
            System.out.println("请输入用户名：");
            ID = scan.next();

            if (!ID.equals("quit") && !ExceptionClass.isDataUnique(Excelor.mangerPath, "Manager", ID, Excelor.Name))
                // 用户名认证通过
                while (!input.equals("quit")) {// 密码验证
                    System.out.println("请输入旧密码：");
                    input = scan.next();
                    if (ExceptionClass.isPasswordCorrect(Excelor.mangerPath, Excelor.sheetName_manager, "admin", input))
                        while (!newPassword.equals("quit") && !input.equals("quit")) {
                            // 密码验证成功，进入新密码验证

                            //
                            String tPassword = "";
                            System.out.println("输入新密码,长度大于等于8个字符，必须是大小写字母、数字和标点符号的组合：");
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

                                                Manager.resetSelfPassword(Excelor.mangerPath, Excelor.ID, ID,
                                                        Excelor.Password, newPassword);

                                                System.out.println("密码已修改，请重新登入");
                                                if_access=false;
                                                scan.close();
                                                return; // 用户名验证通过，退出循环
                                            }
                                        }
                                    } catch (ExceptionClass.PasswordValidationException e) {
                                        System.out.println("密码不符合规范: " + e.getMessage());
                                    }
                                } catch (ExceptionClass.MatchingInputsValidationException e) {
                                    System.out.println(e.getMessage());
                                }
                            }

                        }
                    else {
                        System.out.println("密码错误");
                    }
                } // 密码验证
            else {// 用户名验证失败，继续循环
                if (!ID.equals("quit"))
                    System.out.println("输入用户名错误");
                else {
                    scan.close();
                    return;
                }
            }

        } // 管理员密码修改
        scan.close();
    }

    void userChangePassword() {
        boolean ifquit = false;
        String ID = "";
        String tPassword = "";
        String input = "";
        Scanner scan = new Scanner(System.in);
        int row = -1;
        while (!ID.equals("quit")) {// 管理员密码修改
            System.out.println("***********用户密码修改***********");
            System.out.println("请输入用户名：");
            ID = scan.next();

            if (!ID.equals("quit")
                    && !ExceptionClass.isDataUnique(Excelor.FilePath, Excelor.sheetName_user, ID, Excelor.Name))
            // 用户名认证通过
            {
                row = Excelor.findDataRow(Excelor.FilePath, Excelor.sheetName_user, ID, Excelor.Name);
                while (!input.equals("quit")) {// 密码验证
                    System.out.println("请输入旧密码：");
                    input = scan.next();
                    if (ExceptionClass.isPasswordCorrect(Excelor.FilePath, Excelor.sheetName_user, ID, input))
                        while (!input.equals("quit") && !tPassword.equals("quit")) {
                            // 密码验证成功，进入新密码验证

                            System.out.println("输入新密码,长度大于等于8个字符，必须是大小写字母、数字和标点符号的组合：");
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

                                            Excelor.updateDataInExcel(Excelor.FilePath, Excelor.sheetName_user, row,
                                                    Excelor.Password, tPassword);

                                            System.out.println("密码已修改，请重新登入");
                                            scan.close();
                                            return; // 用户名验证通过，退出循环
                                        }
                                    }
                                } catch (ExceptionClass.PasswordValidationException e) {
                                    System.out.println("密码不符合规范: " + e.getMessage());
                                }
                            } catch (ExceptionClass.MatchingInputsValidationException e) {
                                System.out.println(e.getMessage());
                                continue;
                            }

                        }
                    else {
                        if (!input.equals("quit"))
                            System.out.println("密码错误");
                    }
                } // 密码验证
            } else {// 用户名验证失败，继续循环
                if (!ID.equals("quit"))
                    System.out.println("输入用户名错误");
                else {
                    scan.close();
                    return;
                }
            }

        } // 管理员密码修改
        scan.close();
    }

    void userFindbackPassword() {
        String input = "";

        Scanner scan = new Scanner(System.in);
        while (!input.equals("quit")) {
            String number = "";
            String verify = "";
            String name = "";
            int row = -1;
            System.out.println("***********找回密码***********");
            System.out.println("请输入用户名：");
            name = scan.next();
            if (ExceptionClass.isUsernameUnique(name)) {
                System.out.println("用户名无效");
                continue;
            } else
                row = Excelor.findDataRow(Excelor.FilePath, Excelor.sheetName_user, name, Excelor.Name);
            System.out.println("请选择找回密码的方式：\n1.电话号码 \n2.邮箱");
            input = scan.next();
            while (!input.equals("quit") && !number.equals("quit")) {
                if (input.equals("1"))
                    while (!number.equals("quit")) {
                        System.out.println("请输入电话号码：");
                        number = scan.next();
                        if (!number.equals("quit")) {
                            if (Excelor.getCellValueByRowColumn(Excelor.FilePath, Excelor.sheetName_user, row,
                                    Excelor.TlNumber).equals(number)) {//
                                int min = 10000; // 最小值（包含）
                                int max = 99999; // 最大值（包含）

                                Random random = new Random();
                                String tVerify = Integer.toString(random.nextInt(max - min + 1) + min);
                                System.out.println("验证码（请勿泄露给他人）：" + tVerify);
                                while (!verify.equals("quit")) {
                                    System.out.println("请输入手机验证码:");
                                    verify = scan.next();
                                    if (verify.equals(tVerify)) {
                                        String newPassword = User.findBackPassword(Excelor.FilePath, "User",
                                                Excelor.TlNumber, number);
                                        System.out.println("新密码为:" + newPassword + "\n请重新登入");
                                        scan.close();
                                        return;
                                    } else {
                                        if (!verify.equals("quit"))
                                            System.out.println("验证码有误");
                                        continue;
                                    }
                                }
                            } else
                                System.out.println("输入的电话号码有误:");
                        } else
                            break;// 电话号码输入quit退出密码
                    }
                else if (input.equals("2")) {
                    while (!number.equals("quit")) {
                        System.out.println("请输入邮箱：");
                        number = scan.next();
                        if (!number.equals("quit")) {
                            if (Excelor.getCellValueByRowColumn(Excelor.FilePath, Excelor.sheetName_user, row,
                                    Excelor.email).equals(number)) {
                                int min = 10000; // 最小值（包含）
                                int max = 99999; // 最大值（包含）

                                Random random = new Random();
                                String tVerify = Integer.toString(random.nextInt(max - min + 1) + min);
                                System.out.println("验证码（请勿泄露给他人）：" + tVerify);
                                while (!verify.equals("quit")) {
                                    System.out.println("请输入邮箱验证码:");
                                    verify = scan.next();
                                    if (verify.equals(tVerify)) {
                                        String newPassword = User.findBackPassword(Excelor.FilePath, "User",
                                                Excelor.email, number);
                                        System.out.println("新密码为:" + newPassword + "\n请重新登入");
                                        scan.close();
                                        return;
                                    } else {
                                        if (!verify.equals("quit"))
                                            System.out.println("验证码有误");
                                        continue;
                                    }
                                }
                            } else

                                System.out.println("输入的邮箱有误:");
                        } else
                            break;// 电话号码输入quit退出密码
                    }
                } else {
                    System.out.println("输入的指令无效");
                    break;
                }
            }

        }
        scan.close();
    }

     void addCommodity() {
        Commodity commodity = new Commodity();
        String input = "";
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("**************添加商品**************");
            System.out.println("请输入商品编号：");
            input = scan.next();
            if (input.equals("quit"))
                break;
            else if (!input.matches("\\d+")) {
                System.out.println("商品编号应为纯数字，请重新输入。");
                continue;
            } else {
                commodity.serialNO = input;
                while (true) {
                    System.out.println("请输入商品名称：");
                    input = scan.next();
                    if (input.equals("quit"))
                        break;
                    else {
                        commodity.name = input;
                        while (true) {
                            System.out.println("请输入商品生产厂家：");
                            input = scan.next();
                            if (input.equals("quit"))
                                break;
                            else {
                                commodity.manufacturer = input;
                                while (true) {
                                    System.out.println("请输入生产日期（形如2021-11-12）：");
                                    input = scan.next();
                                    if (input.equals("quit"))
                                        break;
                                    else if (!input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                                        System.out.println("生产日期格式不正确，请重新输入。");
                                        continue;
                                    } else {
                                        commodity.mnDate = input;
                                        while (true) {
                                            System.out.println("请输入型号：");
                                            input = scan.next();
                                            if (input.equals("quit"))
                                                break;
                                            else {
                                                commodity.type = input;
                                                while (true) {
                                                    System.out.println("请输入进货价：");
                                                    input = scan.next();
                                                    if (input.equals("quit"))
                                                        break;
                                                    else {
                                                        try {
                                                            commodity.primeCost = Float.parseFloat(input);
                                                        } catch (NumberFormatException e) {
                                                            System.out.println("请输入有效的数字。");
                                                            continue;
                                                        }
                                                        while (true) {
                                                            System.out.println("请输入零售价格：");
                                                            input = scan.next();
                                                            if (input.equals("quit"))
                                                                break;
                                                            else {
                                                                try {
                                                                    commodity.price = Float.parseFloat(input);
                                                                } catch (NumberFormatException e) {
                                                                    System.out.println("请输入有效的数字。");
                                                                    continue;
                                                                }
                                                                while (true) {
                                                                    System.out.println("请输入数量：");
                                                                    input = scan.next();
                                                                    if (input.equals("quit"))
                                                                        break;
                                                                    else {
                                                                        try {
                                                                            commodity.amount = Integer.parseInt(input);
                                                                            commodity.amount = Integer.parseInt(input);
                                                                            Manager.addCommodity(Excelor.commodityPath,
                                                                                    Excelor.sheetName_commodity,
                                                                                    commodity.serialNO, commodity.name,
                                                                                    commodity.manufacturer,
                                                                                    commodity.mnDate,
                                                                                    commodity.type, commodity.primeCost,
                                                                                    commodity.price, commodity.amount);
                                                                            System.out.println("添加成功");
                                                                            scan.close();
                                                                            return;
                                                                        } catch (NumberFormatException e) {
                                                                            System.out.println("请输入有效的整数。");
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        scan.close();
    }

    void modifyCommodityIF() {
        // Commodity commodity = new Commodity();
        String input = "";
        Scanner scan = new Scanner(System.in);
        int row = -1;

        System.out.println("*******************修改商品信息*******************");

        while (true) {
            System.out.println("请输入商品序列号：");
            input = scan.next();

            if (input.equals("quit")) {
                break;
            }

            row = Excelor.findDataRow(Excelor.commodityPath, Excelor.sheetName_commodity, input, Excelor.Serial_NO);

            if (row > -1) {
                Manager.showSingleCommodity(Excelor.commodityPath, Excelor.sheetName_commodity, row);
                while (true) {
                    System.out.println("1.商品名称\n2.生产厂家\n3.生产日期\n4.产品型号\n5.进货价\n6.零售价格\n7.数量\n请输入对应数字以修改商品信息：");
                    input = scan.next();

                    if (input.equals("quit")) {
                        break;
                    } else {
                        switch (input) {
                            case "1":
                                System.out.println("请输入新的商品名称：");
                                input = scan.next();
                                if (!input.equals("quit")) {
                                    Excelor.updateDataInExcel(Excelor.commodityPath, Excelor.sheetName_commodity, row,
                                            Excelor.Name, input);
                                    System.out.println("商品名称已更新。");
                                }
                                break;
                            case "2":
                                System.out.println("请输入新的生产厂家：");
                                input = scan.next();
                                if (!input.equals("quit")) {
                                    Excelor.updateDataInExcel(Excelor.commodityPath, Excelor.sheetName_commodity, row,
                                            Excelor.Manufacturer, input);
                                    System.out.println("生产厂家已更新。");
                                }
                                break;
                            case "3":
                                System.out.println("请输入新的生产日期（形如2021-11-12）：");
                                input = scan.next();
                                if (!input.equals("quit")) {
                                    if (input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                                        Excelor.updateDataInExcel(Excelor.commodityPath, Excelor.sheetName_commodity,
                                                row, Excelor.MnDate, input);
                                        System.out.println("生产日期已更新。");
                                    } else {
                                        System.out.println("生产日期格式不正确。");
                                    }
                                }
                                break;
                            case "4":
                                System.out.println("请输入新的产品型号：");
                                input = scan.next();
                                if (!input.equals("quit")) {
                                    Excelor.updateDataInExcel(Excelor.commodityPath, Excelor.sheetName_commodity, row,
                                            Excelor.Type, input);
                                    System.out.println("产品型号已更新。");
                                }
                                break;
                            case "5":
                                System.out.println("请输入新的进货价：");
                                input = scan.next();
                                if (!input.equals("quit")) {
                                    try {
                                        float price = Float.parseFloat(input);
                                        Excelor.updateDataInExcel(Excelor.commodityPath, Excelor.sheetName_commodity,
                                                row, Excelor.Prime_Cost, String.valueOf(price));
                                        System.out.println("进货价已更新。");
                                    } catch (NumberFormatException e) {
                                        System.out.println("请输入有效的数字。");
                                    }
                                }
                                break;
                            case "6":
                                System.out.println("请输入新的零售价格：");
                                input = scan.next();
                                if (!input.equals("quit")) {
                                    try {
                                        float price = Float.parseFloat(input);
                                        Excelor.updateDataInExcel(Excelor.commodityPath, Excelor.sheetName_commodity,
                                                row, Excelor.Price, String.valueOf(price));
                                        System.out.println("零售价格已更新。");
                                    } catch (NumberFormatException e) {
                                        System.out.println("请输入有效的数字。");
                                    }
                                }
                                break;
                            case "7":
                                System.out.println("请输入新的数量：");
                                input = scan.next();
                                if (!input.equals("quit")) {
                                    try {
                                        int amount = Integer.parseInt(input);
                                        Excelor.updateDataInExcel(Excelor.commodityPath, Excelor.sheetName_commodity,
                                                row, Excelor.Amount, String.valueOf(amount));
                                        System.out.println("数量已更新。");
                                    } catch (NumberFormatException e) {
                                        System.out.println("请输入有效的整数。");
                                    }
                                }
                                break;
                            default:
                                System.out.println("无效的选项。");
                        }
                    }
                }
            } else {
                System.out.println("未找到对应的商品序列号。");
            }
        }

        scan.close();
    }

    void managerDeleteCommodity() {
        String input = "";
        Scanner scan = new Scanner(System.in);
        int row = -1;
        System.out.println("*******************删除商品信息*******************");

        while (true) {
            System.out.println("请输入商品编号：");
            input = scan.next();
            if (input.equals("quit"))
                break;
            row = Excelor.findDataRowSinceZero(Excelor.commodityPath, Excelor.sheetName_commodity, input,
                    Excelor.Serial_NO);
            if (row > -1) {
                Manager.showSingleCommodity(Excelor.commodityPath, Excelor.sheetName_commodity, row);
                System.out.println("再次输入商品编号确定删除该商品：");
                if (input.equals(scan.next())) {
                    Excelor.deleteDataInExcel(Excelor.commodityPath, Excelor.sheetName_commodity, row);
                    System.out.println("成功删除商品");
                    break;
                } else if (input.equals("quit"))
                    break;
                else
                    continue;
            }

        }
        scan.close();
    }

    void queryUser() {
        String input = "";
        Scanner scan = new Scanner(System.in);
       // int row = -1;
        while (true) {
            System.out.println("*******************查找用户*******************");
            System.out.println("请选择查找方式：\n1.模糊搜索\n2.精确查找\n输入对应数字进入对应查找");
            input = scan.next();
            if (!input.equals("quit"))
                switch (input) {
                    case "1":
                        System.out.println("请输入用户ID或用户名进行模糊搜索");
                        input = scan.next();
                        if (!input.equals("quit")) {
                            Manager.queryUser(Excelor.FilePath, Excelor.sheetName_user, input);
                            break;
                        } else
                            break;
                    case "2":
                        System.out.println("请输入用户ID或用户名进行精确查找");
                        input = scan.next();
                        if (!input.equals("quit")) {
                            Manager.searchUser(Excelor.FilePath, Excelor.sheetName_user, input);
                            break;
                        } else
                            break;
                    default:
                        System.out.println("请输入正确的指令");
                        break;
                }
            else
                break;
        }
        scan.close();

    }

}
