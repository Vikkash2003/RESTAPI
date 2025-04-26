package project.restapi.mapper;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import project.restapi.exceptions.EmptyCartException;
import project.restapi.models.ErrorResponse;

@Provider
public class EmptyCartExceptionMapper implements ExceptionMapper<EmptyCartException> {
    @Override
    public Response toResponse(EmptyCartException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode());
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}