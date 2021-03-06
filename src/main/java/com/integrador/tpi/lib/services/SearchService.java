package com.integrador.tpi.lib.services;

import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.DAL.DocumentDao;
import com.integrador.tpi.lib.domain.DAL.PostsDao;
import com.integrador.tpi.lib.domain.Post;
import com.integrador.tpi.lib.domain.Vocabulary;
import com.integrador.tpi.lib.utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SearchService {
    private static final String splitLineRegex = "[ ,.;-]";
    private static final Pattern splitLinePattern = Pattern.compile(splitLineRegex);

    public static ArrayList<Post> search(String query, HashMap<String, Vocabulary> vocabularyHashMap, DBManager dbManager) {
        int documentCount = DocumentDao.getDocumentsCount(dbManager);

        ArrayList<Vocabulary> consultVocabulary = new ArrayList<>();
        ArrayList<Post> consultPosts = new ArrayList<>();

        String[] terms = splitLinePattern.split(query);
        for (String term : terms) {
            term = Utils.normalizeString(term);

            if (term.equals("")) continue;

            if (vocabularyHashMap.containsKey(term)) {
                Vocabulary vocabularyEntry = vocabularyHashMap.get(term);
                consultVocabulary.add(vocabularyEntry);
            }
        }

        consultVocabulary.sort(Comparator.comparingInt(Vocabulary::getDocumentFrequency));

        for (Vocabulary currentVocabularyTerm : consultVocabulary) {
            HashMap<Integer, Post> currentTermPostsList;
            try {
                currentTermPostsList = PostsDao.getTermPosts(currentVocabularyTerm.getTerm(), dbManager);
                if (currentTermPostsList == null) continue;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            for (Integer documentId : currentTermPostsList.keySet()) {
                Post currentPost = currentTermPostsList.get(documentId);

                if (!consultPosts.contains(currentPost)) {
                    double currentPostTermFrequency = currentPost.getTermFrequency();

                    double relevanceIdx =
                        currentPostTermFrequency * Math.log(documentCount / currentVocabularyTerm.getDocumentFrequency());

                    currentPost.setRelevanceIdx(relevanceIdx);
                    consultPosts.add(currentPost);
                } else {
                    int currentPostIdx = consultPosts.indexOf(currentPost);
                    double currentRelevantIdx = consultPosts.get(currentPostIdx).getRelevanceIdx();

                    int currentPostTermFrequency = consultPosts.get(currentPostIdx).getTermFrequency();
                    double numerator =
                        currentPostTermFrequency * Math.log(documentCount / currentVocabularyTerm.getDocumentFrequency());

                    double newRelevanceIdx =
                        currentRelevantIdx + (numerator);

                    consultPosts.get(currentPostIdx).setRelevanceIdx(newRelevanceIdx);
                }
            }
        }

        consultPosts.sort((post1, post2) -> (int) (post2.getRelevanceIdx() - post1.getRelevanceIdx()));
        return consultPosts;
    }
}
