package com.integrador.tpi.lib.domain.DAL;

import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.Post;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PostsDao {
    public static void save(HashMap<String, HashMap<Integer, Post>> postsHashMap, DBManager dbManager) {
        String insertSql = "INSERT INTO POSTS (DOCUMENT_ID, WORD, FREQUENCY) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement;
        try {
            preparedStatement = dbManager.getNewConnection().prepareStatement(insertSql);
            for (String term : postsHashMap.keySet()) {
                HashMap<Integer, Post> currentTermPosts = postsHashMap.get(term);

                for (Integer documentId : currentTermPosts.keySet()) {
                    Post currentPost = currentTermPosts.get(documentId);

                    preparedStatement.setInt(1, documentId);
                    preparedStatement.setString(2, term);
                    preparedStatement.setInt(3, currentPost.getTermFrequency());
                    preparedStatement.addBatch();
                }
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Post buildPostEntry(ResultSet rs) throws SQLException {
        Post postEntry;
        postEntry = new Post(
            rs.getInt("DOCUMENT_ID"),
            rs.getInt("FREQUENCY"),
            rs.getString("PATH")
        );
        return postEntry;
    }

    public static HashMap<Integer, Post> getTermPosts(String term, DBManager dbManager) {
        String sql =
            "SELECT DOCUMENT_ID, WORD, FREQUENCY, PATH FROM POSTS " +
                "JOIN DOCUMENTS ON DOCUMENTS.ID = POSTS.DOCUMENT_ID " +
                "WHERE WORD LIKE ?";

        try {
            dbManager.prepareQuery(sql);
            dbManager.setString(1, term);

            ResultSet resultSet = dbManager.executeQuery();

            HashMap<Integer, Post> posts = new HashMap<>();
            while (resultSet.next()) {
                Post postEntry = buildPostEntry(resultSet);
                posts.put(postEntry.getDocumentId(), postEntry);
            }
            resultSet.close();
            if (posts.size() > 0) {
                return posts;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
