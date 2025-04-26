package project.restapi.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import project.restapi.exceptions.InvalidInputException;

import java.util.Map;

@Provider
public class InvalidInputExceptionMapper implements ExceptionMapper<InvalidInputException> {
    @Override
    public Response toResponse(InvalidInputException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of(
                        "message", e.getMessage(),
                        "statusCode", Response.Status.BAD_REQUEST.getStatusCode()
                ))
                .build();
    }
}