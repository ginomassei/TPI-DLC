package com.integrador.tpi.api.resources;

import com.integrador.tpi.api.config.VendorConfig;
import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.DAL.PostsDao;
import com.integrador.tpi.lib.domain.DAL.VocabularyDao;
import com.integrador.tpi.lib.domain.Post;
import com.integrador.tpi.lib.domain.Vocabulary;
import com.integrador.tpi.lib.services.IndexService;
import org.apache.commons.io.FileUtils;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Path("/index")
public class IndexResource {
    private static final String DATA_PATH = "/Users/ginomassei/dev/dlc/tpi/documents/";

    @EJB
    VendorConfig vendorConfig;

    @Inject
    private DBManager dbManager;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response index(@QueryParam("fileName") String fileName, String body) {
        HashMap<String, Vocabulary> vocabularyHashMap = vendorConfig.getVocabulary();
        HashMap<String, HashMap<Integer, Post>> postsHashMap = new HashMap<>();

        if (body == null || fileName == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            String filePath = DATA_PATH + fileName;
            FileUtils.writeStringToFile(new File(filePath), body, StandardCharsets.UTF_8);

            BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8));

            IndexService.index(reader, fileName, vocabularyHashMap, dbManager, postsHashMap);
            if (IndexService.areDocumentsIndexed) {
                try {
                    VocabularyDao.save(vocabularyHashMap, dbManager);
                    vendorConfig.reloadConfiguration();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    PostsDao.save(postsHashMap, dbManager);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                return Response
                    .status(Response.Status.CONFLICT)
                    .entity("{\"status\": \"error\", \"message\": \"Document already indexed!\"}")
                    .build();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Response
            .status(Response.Status.OK)
            .entity("{\"status\": \"ok\", \"message\": \"Document indexed successfully!\"}")
            .build();
    }
}
