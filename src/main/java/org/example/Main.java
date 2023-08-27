package org.example;

import java.util.Scanner;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Main {
    

    public static void main(String[] args) {
        System.out.println("Hello world!");
       // Excelor.ExcelCreator();
       Excelor.managerExcelCreator();
        User user=new User();
        //user.register();
        Menu menu=new Menu();
       // menu.resetUserPassword();
       // menu.resetManagerPassword();
    
      
    }

    }
