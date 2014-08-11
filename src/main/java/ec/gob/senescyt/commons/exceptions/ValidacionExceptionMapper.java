package ec.gob.senescyt.commons.exceptions;

import io.dropwizard.jersey.validation.ValidationErrorMessage;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidacionExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private static final int BAD_REQUEST = 400;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        final ValidationErrorMessage message = new ValidationErrorMessage(exception.getConstraintViolations());

        return Response.status(BAD_REQUEST)
                .entity(message)
                .build();
    }
}
