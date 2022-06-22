package com.integrador.tpi.lib.domain.DAL;

import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.Vocabulary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class VocabularyDao {
    public static HashMap<String, Vocabulary> getAll(DBManager dbManager) {
        String sql = "SELECT WORD, MAX_TERM_FREQUENCY, DOCUMENT_FREQUENCY FROM VOCABULARY";
        try {
            PreparedStatement preparedStatement = dbManager.getNewConnection().prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            HashMap<String, Vocabulary> vocabulary = new HashMap<>();
            while (resultSet.next()) {
                Vocabulary vocabularyEntry = buildVocabularyEntry(resultSet);
                vocabulary.put(vocabularyEntry.getTerm(), vocabularyEntry);
            }
            resultSet.close();
            return vocabulary;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void save(HashMap<String, Vocabulary> vocabulary, DBManager dbManager) {
        String sql = "DROP TABLE IF EXISTS VOCABULARY";
        try {
            PreparedStatement preparedStatement = dbManager.getNewConnection().prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            sql = "CREATE TABLE VOCABULARY (WORD VARCHAR(200) NOT NULL, MAX_TERM_FREQUENCY INT NOT NULL, DOCUMENT_FREQUENCY INT NOT NULL, PRIMARY KEY (WORD))";
            PreparedStatement preparedStatement = dbManager.getNewConnection().prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String sqlInsert = "INSERT INTO VOCABULARY (WORD, MAX_TERM_FREQUENCY, DOCUMENT_FREQUENCY) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedInsertStatement = dbManager.getNewConnection().prepareStatement(sqlInsert);
            for (String w: vocabulary.keySet()) {
                Vocabulary vocabularyEntry = vocabulary.get(w);

                preparedInsertStatement.setString(1, vocabularyEntry.getTerm());
                preparedInsertStatement.setInt(2, vocabularyEntry.getMaxFrequency());
                preparedInsertStatement.setInt(3, vocabularyEntry.getDocumentFrequency());
                preparedInsertStatement.addBatch();
            }
            preparedInsertStatement.executeBatch();
            preparedInsertStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Vocabulary buildVocabularyEntry(ResultSet rs) throws SQLException {
        return new Vocabulary(
            rs.getString("WORD"),
            rs.getInt("DOCUMENT_FREQUENCY"),
            rs.getInt("MAX_TERM_FREQUENCY")
        );
    }
}
