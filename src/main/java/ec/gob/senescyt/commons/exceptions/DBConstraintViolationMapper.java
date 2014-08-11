package ec.gob.senescyt.commons.exceptions;

import org.hibernate.exception.ConstraintViolationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

public class DBConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {

    //  Codigos de error de postgres: http://www.postgresql.org/docs/9.3/static/errcodes-appendix.html
    public static final String FOREIGN_KEY_VIOLATION_SQL_CODE = "23503";
    public static final String MENSAJE_COMUN = "no es un valor válido";

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        if (FOREIGN_KEY_VIOLATION_SQL_CODE.equals(exception.getSQLState())) {
            String nombreCampo = exception.getConstraintName().replace("_fkey", "");
            String mensaje = String.format("%s %s", nombreCampo, MENSAJE_COMUN);
            return Response.status(BAD_REQUEST)
                    .entity(mensaje)
                    .build();
        }
        return Response.status(INTERNAL_SERVER_ERROR)
                .entity(exception.getMessage())
                .build();
    }
}
