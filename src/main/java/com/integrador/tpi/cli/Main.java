package com.integrador.tpi.cli;

import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.DAL.PostsDao;
import com.integrador.tpi.lib.domain.DAL.VocabularyDao;
import com.integrador.tpi.lib.domain.Post;
import com.integrador.tpi.lib.domain.Vocabulary;
import com.integrador.tpi.lib.services.IndexService;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

public class Main {
    private static final HashMap<String, Vocabulary> vocabularyHashMap = new HashMap<>();
    private static final HashMap<String, HashMap<Integer, Post>> postsHashMap = new HashMap<>();
    public static void main(String[] args) {
        DBManager db = new DBManager();
        db.setConnectionMode(DBManager.DBConnectionMode.SINGLE_CONNECTION_MODE);
        db.setDriverName(DBManager.MARIADB_DRIVER_NAME);
        db.setUrl("jdbc:mariadb://localhost:3306/dlc?rewriteBatchedStatements=true");
        db.setUserName("root");
        db.setPassword("dlc2022");
        try {
            db.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String DATA_PATH = "/Users/ginomassei/dev/dlc/tpi/documents/";

        try {
            run(DATA_PATH, db);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            saveVocabulary(db);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void run(String DATA_PATH, DBManager dbManager) throws Exception {
        File folder = new File(DATA_PATH);
        File[] documentList = folder.listFiles();

        assert documentList != null;

        for (File document : documentList) {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(Files.newInputStream(document.toPath()), StandardCharsets.UTF_8));
            IndexService.index(reader, document.getName(), vocabularyHashMap, dbManager, postsHashMap);
        }
        PostsDao.save(postsHashMap, dbManager);
    }

    private static void saveVocabulary(DBManager dbManager) throws Exception {
        if (IndexService.areDocumentsIndexed) {
            System.out.println("Saving vocabulary.\n");
            VocabularyDao.save(vocabularyHashMap, dbManager);
            System.out.println("Vocabulary saved.\n");
            IndexService.areDocumentsIndexed = false;
        }
    }
}
