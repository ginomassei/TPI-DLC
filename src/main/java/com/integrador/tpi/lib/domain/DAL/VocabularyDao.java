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
        String sqlInsert = "INSERT INTO VOCABULARY (WORD, MAX_TERM_FREQUENCY, DOCUMENT_FREQUENCY) VALUES (?, ?, ?)";
        String sqlUpdate = "UPDATE VOCABULARY SET MAX_TERM_FREQUENCY = ?, DOCUMENT_FREQUENCY = ? WHERE WORD = ?";

        try {
            PreparedStatement preparedInsertStatement = dbManager.getNewConnection().prepareStatement(sqlInsert);
            PreparedStatement preparedUpdateStatement = dbManager.getNewConnection().prepareStatement(sqlUpdate);

            for (String w: vocabulary.keySet()) {
                Vocabulary vocabularyEntry = vocabulary.get(w);

                if (vocabularyEntry.isNew()) {
                    System.out.println("Inserting: " + vocabularyEntry.getTerm());
                    preparedInsertStatement.setString(1, vocabularyEntry.getTerm());
                    preparedInsertStatement.setInt(2, vocabularyEntry.getMaxFrequency());
                    preparedInsertStatement.setInt(3, vocabularyEntry.getDocumentFrequency());
                    preparedInsertStatement.addBatch();
                }
                if (vocabularyEntry.needsUpdate()) {
                    System.out.println("Updating " + vocabularyEntry.getTerm());
                    preparedUpdateStatement.setInt(1, vocabularyEntry.getMaxFrequency());
                    preparedUpdateStatement.setInt(2, vocabularyEntry.getDocumentFrequency());
                    preparedUpdateStatement.setString(3, vocabularyEntry.getTerm());
                    preparedUpdateStatement.addBatch();
                }
            }
            preparedInsertStatement.executeBatch();
            preparedUpdateStatement.executeBatch();
            preparedInsertStatement.close();
            preparedUpdateStatement.close();
            System.out.println("Vocabulary saved");
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
