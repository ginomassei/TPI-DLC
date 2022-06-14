package com.integrador.tpi.cli;

import com.integrador.tpi.lib.db.DBManager;

public class Main {
    public static void main(String[] args) {
        DBManager db = new DBManager();
        db.setConnectionMode(DBManager.DBConnectionMode.SINGLE_CONNECTION_MODE);
        db.setDriverName(DBManager.MYSQL_DRIVER_NAME);
        db.setUrl("jdbc:mysql://localhost:3306/DLC_DB?rewriteBatchedStatements=true");
        db.setUserName("root");
        db.setPassword("dlc2022");
        try {
            db.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("DBManager connected");
    }
}
