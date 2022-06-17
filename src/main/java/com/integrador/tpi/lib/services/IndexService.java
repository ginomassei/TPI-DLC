package com.integrador.tpi.lib.services;

import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.DAL.DocumentDao;
import com.integrador.tpi.lib.domain.DAL.PostsDao;
import com.integrador.tpi.lib.domain.Post;
import com.integrador.tpi.lib.domain.Vocabulary;
import com.integrador.tpi.lib.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class IndexService {
    private static final String splitLineRegex = "[ ,.;-]";
    private static final Pattern splitLinePattern = Pattern.compile(splitLineRegex);
    public static boolean areDocumentsIndexed = false;

    public static void index(
        BufferedReader document,
        String documentName,
        HashMap<String, Vocabulary> vocabularyHashMap,
        DBManager dbManager,
        HashMap<String, HashMap<Integer, Post>> postsHashMap
    ) throws IOException {
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

                HashMap<Integer, Post> currentTermPosts = PostsDao.getTermPosts(currentTerm, dbManager);
                if (currentTermPosts == null) {
                    currentTermPosts = new HashMap<>();
                    Post currentPost = new Post(documentId, 1);

                    currentTermPosts.put(documentId, currentPost);
                    postsHashMap.put(currentTerm, currentTermPosts);

                    Vocabulary newEntry = new Vocabulary(currentTerm);
                    newEntry.setMaxFrequency(1);

                    vocabularyHashMap.put(currentTerm, newEntry);
                    continue;
                }

                if (!currentTermPosts.containsKey(documentId)) {
                    Post currentPost = new Post(documentId, 1);
                    currentTermPosts.put(documentId, currentPost);
                    postsHashMap.put(currentTerm, currentTermPosts);

                    Vocabulary vocabularyEntry = vocabularyHashMap.get(currentTerm);
                    vocabularyEntry.incrementDocumentFrequency();

                    vocabularyHashMap.put(currentTerm, vocabularyEntry);
                }

                if (currentTermPosts.containsKey(documentId)) {
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
    }
}
