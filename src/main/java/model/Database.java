package model;

import java.util.*;


public class Database {
    private static Database instance;





    public static Database getInstance(){
        if(instance == null)
            instance = new Database();
        return instance;
    }

    public static void setInstance(Database instance) {
        Database.instance = instance;
    }


}
