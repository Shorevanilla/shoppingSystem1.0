package org.example;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Menu menu = new Menu();
        String input = "";
        boolean ifcointinue=true;
        Scanner scan = new Scanner(System.in);
        while (true) {
            
            System.out.println("************ 简易购物系统 ************");
            System.out.println("1.管理员登入");
            System.out.println("2.用户登入");
            System.out.println("3.用户注册");
            System.out.println("任何时候可输入quit返回");
            System.out.println("请输入对应指令:");
            ifcointinue=true;
            input = scan.next();
            switch (input) {
                case "1":
                    menu.managerLogIN();
                    if (menu.if_access) {
                        while (ifcointinue) {
                            System.out.println("************ 管理员操作界面 ************");
                            System.out.println("1.添加商品");
                            System.out.println("2.修改商品信息");
                            System.out.println("3.删除商品");
                            System.out.println("4.修改管理员密码");
                            System.out.println("5.查找用户信息");
                            System.out.println("任何时候可输入quit返回");
                            System.out.println("请输入对应指令:");
                          //input=scan.next();
                            switch (input) {
                                case "1":
                                    menu.addCommodity();continue;
                                    
                                case "2":
                                case "3":
                                case "4":
                                case "5":

                                case "quit":
                                    ifcointinue=false;
                                    break;
                                default:
                                    System.out.println("请输入正确指令:");
                            }
                        }
                    }
                    break;
                case "2": break;
                case "3": break;
                case "quit":
                scan.close();
                return;
                default:
                    System.out.println("请输入正确指令:");
            }
        }
        
    }

}
