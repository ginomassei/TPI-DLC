package com.integrador.tpi.api;

import com.integrador.tpi.lib.db.DBManager;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.sound.midi.Soundbank;

@ApplicationScoped
public class DBManagerProducer {

    @Produces
    @RequestScoped
    public DBManager create() {
        DBManager db = null;
        try {
            db = new DBManager();
            db.setConnectionMode(DBManager.DBConnectionMode.SINGLE_CONNECTION_MODE);
            db.setDriverName(DBManager.MYSQL_DRIVER_NAME);
            db.setUrl("jdbc:mysql://localhost:3306/DLC?rewriteBatchedStatements=true");
            db.setUserName("root");
            db.setPassword("dlc2022");
            db.connect();
            System.out.println("DBManager connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return db;
    }

    public void destroy(@Disposes DBManager db) {
        if (db != null) db.close();
    }
}
