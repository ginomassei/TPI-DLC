package com.integrador.tpi.api.resources;

import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.DAL.DocumentDao;
import com.integrador.tpi.lib.domain.Document;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/hello-world")
public class HelloResource {

    @Inject
    private DBManager db;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello() {
        ArrayList<Document> count = DocumentDao.getAll(db);

        return Response
            .status(Response.Status.OK)
            .type(MediaType.APPLICATION_JSON)
            .entity(count)
            .build();
    }
}
