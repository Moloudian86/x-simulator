package model;

import java.io.Serializable;
import java.util.*;

public class NormalUser extends User implements Serializable {
    public NormalUser(String fullName,String username,String email,String phone, String password){
        super(fullName,username,email,phone, password);
    }
}

// رعایت اصل دوم و سوم :
// ارث بری از کلاس user برای توسعه مثلا اگه بعدا یه کلاس DiamondUser خواستیم اضافه کنیم راحت میتونیم انجامش بدیم
//و کلاس فرزند میتونه جایگزین کلاس پدر بشه