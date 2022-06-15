package com.integrador.tpi.api.resources;

import com.integrador.tpi.api.config.VendorConfig;
import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.DAL.VocabularyDao;
import com.integrador.tpi.lib.domain.Vocabulary;
import com.integrador.tpi.lib.services.IndexService;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

@Path("/index")
public class IndexResource {
    @EJB
    VendorConfig vendorConfig;

    @Inject
    private DBManager db;

    private final HashMap<String, Vocabulary> vocabularyHashMap = new HashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response index() {
        try {
            String DATA_PATH = "/Users/ginomassei/dev/dlc/tpi/documents/";
            this.run(DATA_PATH, db);
            this.saveVocabulary(db);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Response
            .status(Response.Status.OK)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }

    private void saveToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
        try {
            OutputStream out = null;
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(uploadedFileLocation);
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run(String DATA_PATH, DBManager dbManager) throws Exception {
        File folder = new File(DATA_PATH);
        File[] documentList = folder.listFiles();

        assert documentList != null;

        for (File document : documentList) {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(Files.newInputStream(document.toPath()), StandardCharsets.UTF_8));
            IndexService.index(reader, document.getName(), vocabularyHashMap, dbManager);
        }
    }

    private void saveVocabulary(DBManager dbManager) throws Exception {
        if (IndexService.areDocumentsIndexed) {
            System.out.println("Saving vocabulary.\n");
            VocabularyDao.save(vocabularyHashMap, dbManager);
            System.out.println("Vocabulary saved.\n");
            IndexService.areDocumentsIndexed = false;
        }
    }
}
