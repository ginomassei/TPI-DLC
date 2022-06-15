package com.integrador.tpi.lib.domain.DAL;

import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.Vocabulary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class VocabularyDao {
    public static HashMap<String, Vocabulary> getAll(DBManager dbManager) {
        String sql = "SELECT * FROM VOCABULARY";

        try {
            dbManager.prepareQuery(sql);

            ResultSet resultSet = dbManager.executeQuery();

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

    public static void save(HashMap<String, Vocabulary> vocabulary, DBManager dbManager) throws Exception {
        String sql =
            "INSERT INTO VOCABULARY (WORD, MAX_TERM_FREQUENCY, DOCUMENT_FREQUENCY) VALUES (?, ?, ?)" +
                "ON DUPLICATE KEY UPDATE MAX_TERM_FREQUENCY = VALUES(MAX_TERM_FREQUENCY), DOCUMENT_FREQUENCY = VALUES(DOCUMENT_FREQUENCY)";

        PreparedStatement preparedStatement = dbManager.getNewConnection().prepareStatement(sql);

        for (String w: vocabulary.keySet()) {
            Vocabulary vocabularyEntry = vocabulary.get(w);
            preparedStatement.setString(1, vocabularyEntry.getTerm());
            preparedStatement.setInt(2, vocabularyEntry.getMaxFrequency());
            preparedStatement.setInt(3, vocabularyEntry.getDocumentFrequency());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
    }

    public static Vocabulary buildVocabularyEntry(ResultSet rs) throws SQLException {
        return new Vocabulary(
            rs.getString("WORD"),
            rs.getInt("DOCUMENT_FREQUENCY"),
            rs.getInt("MAX_TERM_FREQUENCY")
        );
    }
}
