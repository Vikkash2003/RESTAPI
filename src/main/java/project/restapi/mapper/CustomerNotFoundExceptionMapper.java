package project.restapi.mapper;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import project.restapi.exceptions.CartNotFoundException;
import project.restapi.exceptions.CustomerNotFoundException;
import project.restapi.models.ErrorResponse;


@Provider
public class CustomerNotFoundExceptionMapper implements ExceptionMapper<CustomerNotFoundException> {
    @Override
    public Response toResponse(CustomerNotFoundException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode());
        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}