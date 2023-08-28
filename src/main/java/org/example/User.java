package org.example;

import java.util.Scanner;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.security.SecureRandom;
public class User {
    String ID;
    String name;
    String password;
    int userLevel;
    String email;
    String TlNumber;
    float consumed;
    boolean ifLocked;

    public static String findBackPassword(String filePath,String sheetName,int findBackType,String number){
    String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digitChars = "0123456789";
        String specialChars = "!@#$%^&*()-_+=<>?";

        String allChars = lowercaseChars + uppercaseChars + digitChars + specialChars;
        int length = 8;

        SecureRandom random = new SecureRandom();
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allChars.length());
            char randomChar = allChars.charAt(randomIndex);
            randomString.append(randomChar);
        }
        String newPassword=randomString.toString();
    Excelor.updateDataInExcel(filePath, Excelor.sheetName_user, findBackType,  number,  Excelor.Password,newPassword);
    return newPassword;  
}

}