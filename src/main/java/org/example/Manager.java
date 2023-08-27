package org.example;
import java.util.UUID;
public class Manager {
    String name;
    String password;
    static String resetUserPassword (String filePath, int searchColumnIndex, String searchValue, int targetColumnIndex){
        String newPassword = UUID.randomUUID().toString()+ "Aa!" ;
        Excelor.updateDataInExcel(filePath, Excelor.sheetName_user, searchColumnIndex,  searchValue,  targetColumnIndex,newPassword);
   return newPassword;
    }
    static void resetSelfPassword(String filePath, int searchColumnIndex, String searchValue, int targetColumnIndex,String newPassword){
        
        Excelor.updateDataInExcel(filePath, Excelor.sheetName_manager, searchColumnIndex,  searchValue,  targetColumnIndex,newPassword);
    }
    void displayUserIF(){

    }
    
}
