package model;

import java.util.*;

public class NormalUser extends User{
    public NormalUser(String fullName,String username,String email,String phone, String password){
        super(fullName,username,email,phone, password);
    }
    public int calculatePostCost(String text, boolean hasMedia) {
        int cost = text.length();
        if (hasMedia) {
            cost += 10;
        }
        return cost;
    }
}

// رعایت اصل دوم و سوم :
// ارث بری از کلاس user برای توسعه مثلا اگه بعدا یه کلاس DiamondUser خواستیم اضافه کنیم راحت میتونیم انجامش بدیم
//و کلاس فرزند میتونه جایگزین کلاس پدر بشه