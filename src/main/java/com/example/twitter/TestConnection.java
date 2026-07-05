package com.example.twitter;

import connection.ConnectionDB;

public class TestConnection {
    public static void main(String[] args) {
        try {
            java.sql.Connection conn = ConnectionDB.getConnection();
            System.out.println("Connected ✅");
        } catch (Exception e) {
            System.out.println("Connection Failed ❌");
            e.printStackTrace();
        }
    }
}