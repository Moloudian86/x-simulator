package model;

import java.util.Date;

public class Admin extends Account{
    private static Admin instance;
    private Admin(){
        super("ADMIN","z","ADMIN1234@gmail.com","09111111111","z");
    }

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }
}
