package com.integrador.tpi.lib.services;

import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.DAL.DocumentDao;
import com.integrador.tpi.lib.domain.DAL.PostsDao;
import com.integrador.tpi.lib.domain.Post;
import com.integrador.tpi.lib.domain.Vocabulary;
import com.integrador.tpi.lib.utils.Utils;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.regex.Pattern;

public class IndexService {
    private static final String splitLineRegex = "[ ,.;-]";
    private static final Pattern splitLinePattern = Pattern.compile(splitLineRegex);

    private static final HashMap<String, HashMap<Integer, Post>> postsHashMap = new HashMap<>();
    public static boolean areDocumentsIndexed = false;

    public static void index(BufferedReader document, String documentName, HashMap<String, Vocabulary> vocabularyHashMap,  DBManager dbManager) throws Exception {
        Integer documentId;
        if ((DocumentDao.getDocumentId(documentName, dbManager)) == null) {
            DocumentDao.save(documentName, dbManager);
            documentId = DocumentDao.getDocumentId(documentName, dbManager);
            areDocumentsIndexed = true;
        } else {
            return;
        }

        String line;
        while ((line = document.readLine()) != null) {
            for (String currentTerm : splitLinePattern.split(line)) {
                currentTerm = Utils.normalizeString(currentTerm);

                if (currentTerm.equals("")) continue;

                HashMap<Integer, Post> currentTermPosts = postsHashMap.get(currentTerm);
                if (currentTermPosts == null) {
                    currentTermPosts = new HashMap<>();

                    Post currentPost = new Post(documentId, 1);
                    currentTermPosts.put(documentId, currentPost);
                    postsHashMap.put(currentTerm, currentTermPosts);

                    Vocabulary newEntry = new Vocabulary(currentTerm, 1, 1);
                    vocabularyHashMap.put(currentTerm, newEntry);
                } else if (currentTermPosts.get(documentId) == null) {
                    Post currentPost = new Post(documentId, 1);
                    currentTermPosts.put(documentId, currentPost);
                    postsHashMap.put(currentTerm, currentTermPosts);

                    Vocabulary vocabularyEntry = vocabularyHashMap.get(currentTerm);
                    vocabularyEntry.incrementDocumentFrequency();
                    vocabularyHashMap.put(currentTerm, vocabularyEntry);
                } else {
                    Post currentPost = currentTermPosts.get(documentId);
                    currentPost.incrementTermFrequency();
                    currentTermPosts.put(documentId, currentPost);
                    postsHashMap.put(currentTerm, currentTermPosts);

                    Vocabulary vocabularyEntry = vocabularyHashMap.get(currentTerm);
                    if (currentPost.getTermFrequency() > vocabularyEntry.getMaxFrequency()) {
                        vocabularyEntry.setMaxFrequency(currentPost.getTermFrequency());
                        vocabularyHashMap.put(currentTerm, vocabularyEntry);
                    }
                }
            }
        }
        PostsDao.save(postsHashMap, dbManager);
        postsHashMap.clear();
    }
}
