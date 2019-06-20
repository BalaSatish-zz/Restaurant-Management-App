package com.satish.bunny.tanga.FireClasses;

/**
 * Created by Bunny on 9/16/2017.
 */

public class Form {
    public String FirstName, LastName, NickName, MobileNumber,UserType;

    public Form() {

    }

    public Form(String firstName, String lastName, String nickName, String mobileNumber, String userType) {
        FirstName = firstName;
        LastName = lastName;
        NickName = nickName;
        MobileNumber = mobileNumber;
        UserType = userType;
    }
}