package org.example;

import java.util.Scanner;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Menu {
    // String input;

    boolean if_access = false;

    void userlogIN() {

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
                                Excelor.updateDataInExcel(Excelor.FilePath,Excelor.sheetName_user, Excelor.TlNumber, input, Excelor.ifLocked,
                                        String.valueOf(0));
                                System.out.println("登入成功");
                                break;
                            } else if (!input2.equals("quit")) {
                                System.out.println("密码错误请，重新输入密码：");
                                ifLocked++;
                                Excelor.updateDataInExcel(Excelor.FilePath,Excelor.sheetName_user, Excelor.TlNumber, input, Excelor.ifLocked,
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
                                Excelor.updateDataInExcel(Excelor.FilePath,Excelor.sheetName_user, Excelor.Name, input, Excelor.ifLocked,
                                        String.valueOf(0));
                                System.out.print("登入成功");
                                break;
                            } else if (!input2.equals("quit")) {
                                System.out.println("密码错误请，重新输入密码：");
                                ifLocked++;
                                Excelor.updateDataInExcel(Excelor.FilePath,Excelor.sheetName_user, Excelor.Name, input, Excelor.ifLocked,
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

   
    void resetManagerPassword() {
        boolean ifquit = false;
        String ID = "";
        String newPassword="";
        String input="";
        Scanner scan = new Scanner(System.in);

        while (!ID.equals("quit")) {//管理员密码修改
            System.out.println("***********管理员密码修改***********");
            System.out.println("请输入用户名：");
            ID = scan.next();
           
            if (!ID.equals("quit")&&!ExceptionClass.isDataUnique(Excelor.mangerPath,"Manager", ID, Excelor.Name)) 
           //用户名认证通过
            while(!input.equals("quit")){//密码验证
                System.out.println("请输入旧密码：");
                input = scan.next();
                if(ExceptionClass.isPasswordCorrect(Excelor.mangerPath,Excelor.sheetName_manager,"admin",input))
                while(!newPassword.equals("quit")&&!input.equals("quit"))
                {
                    //密码验证成功，进入新密码验证
                    
                    
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
                                   
                                    Manager.resetSelfPassword(Excelor.mangerPath, Excelor.ID, ID, Excelor.Password,newPassword);
                                    
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
                    }
                }      
                    
            }
               else{
                System.out.println("密码错误");
               }
            }//密码验证
             else {//用户名验证失败，继续循环
                if(!ID.equals("quit"))
                System.out.println("输入用户名错误");
                else 
                {
                    scan.close();
                    return;   
                }
            }
            
        }//管理员密码修改
        scan.close();
    }
    
}
