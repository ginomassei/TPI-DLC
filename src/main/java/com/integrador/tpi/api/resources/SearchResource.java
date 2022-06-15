package com.integrador.tpi.api.resources;

import com.integrador.tpi.api.config.VendorConfig;
import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.Post;
import com.integrador.tpi.lib.domain.Vocabulary;
import com.integrador.tpi.lib.services.SearchService;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;

@Path("/search")
public class SearchResource {
    @EJB
    VendorConfig vendorConfig;

    @Inject
    private DBManager dbManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response runQuery(
        @QueryParam("query") String query,
        @QueryParam("limit") int limit) {
        HashMap<String, Vocabulary> vocabularyHashMap = vendorConfig.getVocabulary();

        ArrayList<Post> posts;
        try {
            posts = SearchService.search(query, vocabularyHashMap, dbManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            return Response
                .status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON)
                .entity(posts)
                .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
