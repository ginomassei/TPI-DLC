package com.integrador.tpi.api.config;

import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.DAL.VocabularyDao;
import com.integrador.tpi.lib.domain.Vocabulary;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.HashMap;

@Singleton
@Startup
public class VendorConfig {

    @Inject
    private DBManager db;

    private HashMap<String, Vocabulary> vocabulary;

    @PostConstruct
    public void loadConfiguration() {
        vocabulary = VocabularyDao.getAll(db);
    }

    public HashMap<String, Vocabulary> getVocabulary() {
        return this.vocabulary;
    }
}
