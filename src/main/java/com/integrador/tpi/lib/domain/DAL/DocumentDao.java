package com.integrador.tpi.lib.domain.DAL;

import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.Document;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DocumentDao {
    public static void save(String documentPath, DBManager dbManager) {
        String SQL_QUERY = "INSERT INTO DOCUMENTS (PATH) VALUES (?)";
        try {
            dbManager.prepareQuery(SQL_QUERY);
            dbManager.setString(1, documentPath);
            dbManager.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(File document, DBManager dbManager) {
        String SQL_QUERY = "DELETE FROM DOCUMENTS WHERE PATH = ?";
        try {
            dbManager.prepareQuery(SQL_QUERY);
            dbManager.setString(1, document.getPath());
            dbManager.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update(File document, DBManager dbManager) {
        String SQL_QUERY = "UPDATE DOCUMENTS SET PATH = ? WHERE PATH = ?";
        try {
            dbManager.prepareQuery(SQL_QUERY);
            dbManager.setString(1, document.getPath());
            dbManager.setString(2, document.getPath());
            dbManager.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Integer getDocumentId(String documentPath, DBManager dbManager) {
        try {
            String SQL_QUERY = "SELECT ID FROM DOCUMENTS WHERE PATH = ?";
            dbManager.prepareQuery(SQL_QUERY);
            dbManager.setString(1, documentPath);
            ResultSet resultSet = dbManager.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getDocumentsCount(DBManager dbManager) {
        String SQL_QUERY = "SELECT COUNT(*) FROM DOCUMENTS";
        try {
            dbManager.prepareQuery(SQL_QUERY);
            ResultSet resultSet = dbManager.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<Document> getAll(DBManager dbManager) {
        ArrayList<Document> documents = new ArrayList<>();

        String SQL_QUERY = "SELECT * FROM DOCUMENTS";
        try {
            dbManager.prepareQuery(SQL_QUERY);
            ResultSet resultSet = dbManager.executeQuery();
            if (resultSet.next()) {
                documents.add(
                    new Document(
                        resultSet.getInt("ID"),
                        resultSet.getString("PATH")
                    ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documents;
    }
}
