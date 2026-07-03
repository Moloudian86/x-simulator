package model;

import java.util.*;

public class GoldUser extends PremiumUser{
    public GoldUser(String fullName,String username,String email,String phone, String password){
        super(fullName,username,email,phone, password);
    }

    @Override
    public int calculatePostCost(String text, boolean hasMedia) {
        return 5;
    }
}
