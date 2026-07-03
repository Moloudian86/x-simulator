package model;

import java.util.*;

public class BlueUser extends PremiumUser{
    
    public BlueUser(String fullName,String username,String email,String phone, String password){
        super(fullName,username,email,phone, password);
    }

    @Override
    public int calculatePostCost(String text, boolean hasMedia) {
        int cost = text.length() / 2;
        if (hasMedia) {
            cost += 5;
        }
        return cost;
    }
}

