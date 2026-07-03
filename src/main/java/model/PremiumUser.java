package model;

import java.util.*;

public abstract class PremiumUser extends User{
    private Date subscriptionDate;
    public PremiumUser(String fullName,String username,String email,String phone, String password){

        super(fullName,username,email,phone, password);
        this.subscriptionDate = new Date();

    }

}
