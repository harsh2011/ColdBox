package com.hvacci.vaccination;



import com.hvacci.vaccination.java.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Harsh on 31-08-2016.
 */
public class UserFile {
    public void setCurrentUser(User user, File FilesDir){
        String data = user.getUsername()+"\n"+user.getFullname()+"\n"+user.getPhone_number()+"\n"+user.getBirthdate()+"\n"+user.getEmail_id()+"\n";

        FileOutputStream outputStream;
        File file;
        try {
            file = new File(FilesDir, "UserDetails.txt");

            outputStream = new FileOutputStream(file);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public User getCurrentUser(File FilesDir){
        User currentUser = new User();
        BufferedReader input = null;
        File rfile = null;
        try {
            rfile = new File(FilesDir, "UserDetails.txt"); // Pass getFilesDir() and "MyFile" to read file

            input = new BufferedReader(new InputStreamReader(new FileInputStream(rfile)));
            String username= input.readLine();
            String fullname= input.readLine();
            String phone= input.readLine();
            String birthdate= input.readLine();
            String email  =  input.readLine();


            currentUser.setUsername(username);
            currentUser.setFullname(fullname);
            currentUser.setPhone_number(phone);
            currentUser.setBirthdate(birthdate);
            currentUser.setEmail_id(email);

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentUser;
    }
    public boolean clearCurrentUser(File FilesDir){
        File user = new File(FilesDir,"UserDetails.txt");
        if(user.exists()){
            user.delete();
            return true;
        }
        return false;
    }
    public boolean checkUserDetails(File FilesDir){
        File user = new File(FilesDir,"UserDetails.txt");
        if(user.exists()){
            return true;
        }
        return false;
    }


}

