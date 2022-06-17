package com.integrador.tpi.api.resources;

import com.integrador.tpi.lib.db.DBManager;
import com.integrador.tpi.lib.domain.DAL.DocumentDao;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.File;

@Path("/files")
public class FilesResource {
    @Inject
    DBManager dbManager;

    private static final String DATA_PATH = "/Users/ginomassei/dev/dlc/tpi/documents/";

    @GET
    @Produces("text/plain")
    @Path("download/{id}")
    public Response getFile(@PathParam("id") String id) {
        String fileName = DocumentDao.getDocumentPath(Integer.parseInt(id), dbManager);
        File file = new File(DATA_PATH + fileName);

        Response.ResponseBuilder response = Response.ok(file);
        response.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        return response.build();
    }
}
