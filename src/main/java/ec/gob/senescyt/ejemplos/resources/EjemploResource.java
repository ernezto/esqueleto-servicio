package ec.gob.senescyt.ejemplos.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ejemplos")
@Produces(MediaType.APPLICATION_JSON)
public class EjemploResource {

    @GET
    public Response obtenerTodos() {
        return Response.ok().build();
    }
}
