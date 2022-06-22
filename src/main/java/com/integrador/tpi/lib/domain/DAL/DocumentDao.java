package com.integrador.tpi.lib.domain.DAL;

import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.Document;

import java.sql.ResultSet;
import java.util.ArrayList;

public class DocumentDao {
    public static int save(String documentPath, DBManager dbManager) {
        String SQL_QUERY = "INSERT INTO DOCUMENTS (PATH) VALUES (?)";
        try {
            dbManager.prepareQuery(SQL_QUERY);
            dbManager.setString(1, documentPath);
            ResultSet rs = dbManager.executeUpdate().getGeneratedKeys();
            if (rs.first()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getDocumentId(String documentPath, DBManager dbManager) {
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
        return -1;
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
            dbManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documents;
    }

    public static String getDocumentPath(Integer documentId, DBManager dbManager) {
        try {
            String SQL_QUERY = "SELECT PATH FROM DOCUMENTS WHERE ID = ?";
            dbManager.prepareQuery(SQL_QUERY);
            dbManager.setInt(1, documentId);
            ResultSet resultSet = dbManager.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("PATH");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
