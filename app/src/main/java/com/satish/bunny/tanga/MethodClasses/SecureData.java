package com.satish.bunny.tanga.MethodClasses;

/**
 * Created by Bunny on 11/5/2017.
 */

public class SecureData {
    public SecureData() {
    }
    public String Encrypt(String text){
        int Key = 4;
        String Result="";
        char Character;
        char ch[]=new char[text.length()];
        for (int i=0;i<text.length();i++)
        {
            Character = text.charAt(i);
            Character = (char) (Character);
            ch[i]=Character;
        }
        Result = String.valueOf(ch);
        return Result;
    }
    public String Decrypt(String text){
        int Key = 4;
        String Result="";
        char Character;
        char ch[]=new char[text.length()];
        for(int i=0;i<text.length();i++)
        {
            Character = text.charAt(i);
            Character = (char) (Character);
            ch[i]=Character;
        }
        Result = String.valueOf(ch);
        return  Result;
    }
}
