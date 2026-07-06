package model;

import java.util.*;


public class Database {
    private static Database instance;


    private List<Report> reports = new ArrayList<>();


    public static Database getInstance(){
        if(instance == null)
            instance = new Database();
        return instance;
    }

    public static void setInstance(Database instance) {
        Database.instance = instance;
    }


    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

}
