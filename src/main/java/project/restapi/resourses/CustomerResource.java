package project.restapi.resourses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.restapi.exceptions.CustomerNotFoundException;
import project.restapi.exceptions.InvalidInputException;
import project.restapi.models.Customer;
import project.restapi.utills.DataStore;

import java.util.Map;

import static project.restapi.utills.DataStore.author;

@Path("/customers")
public class CustomerResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response createCustomer(Customer customer) {
        if (customer == null) {
            throw new CustomerNotFoundException("Customer details cannot be empty");
        }

        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name cannot be empty");
        }

        if (customer.getEmail() == null || !customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidInputException("Invalid email format");
        }

        int newId = DataStore.customer.size() + 1;
        customer.setId(newId);
        DataStore.customer.put(newId, customer);
        return Response.status(Response.Status.CREATED)
                .entity(customer)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers() {
        if (DataStore.customer.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of(
                            "message", "No customers found in the system",
                            "statusCode", 404
                    ))
                    .build();
        }
        return Response.ok(DataStore.customer.values()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("id") int id) {
        Customer customer = DataStore.customer.get(id);
        if (customer == null) {
            throw new CustomerNotFoundException(id);
        }
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {
        if (updatedCustomer == null) {
            throw new CustomerNotFoundException("Customer details cannot be empty");
        }

        if (!DataStore.customer.containsKey(id)) {
            throw new CustomerNotFoundException(id);
        }

        updatedCustomer.setId(id);
        DataStore.customer.put(id, updatedCustomer);
        return Response.ok(updatedCustomer).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCustomer(@PathParam("id") int id) {
        if (!DataStore.customer.containsKey(id)) {
            throw new CustomerNotFoundException(id);
        }

        DataStore.customer.remove(id);
        return Response.ok()
                .entity(Map.of(
                        "message", "Customer with ID: " + id + " deleted successfully",
                        "statusCode", 200
                ))
                .build();
    }
}