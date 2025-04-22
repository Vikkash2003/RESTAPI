package project.restapi.resourses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.restapi.models.Customer;
import project.restapi.utills.DataStore;

@Path("/customers")
public class CustomerResource {

    //add new customer
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomer(Customer customer) {
        int newId = DataStore.customer.size() + 1;
        customer.setId(newId);
        DataStore.customer.put(newId, customer);
        return Response.status(Response.Status.CREATED)
                .entity(customer)
                .build();
    }

    //get all customer
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers() {
        if (DataStore.customer.isEmpty()) {
            return Response.ok("[]").build();
        }

        return Response.ok(DataStore.customer.values()).build();
    }

    //get customer by Id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("id") int id) {
        Customer customer = DataStore.customer.get(id);

        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Customer not found with ID: " + id)
                    .build();
        }

        return Response.ok(customer).build();
    }

    //update customer
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {
        if (!DataStore.customer.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Customer not found with ID: " + id)
                    .build();
        }

        updatedCustomer.setId(id);
        DataStore.customer.put(id, updatedCustomer);

        return Response.ok(updatedCustomer).build();
    }

    //delete customer by id
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCustomer(@PathParam("id") int id) {
        if (!DataStore.customer.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Customer not found with ID: " + id)
                    .build();
        }

        Customer removedCustomer = DataStore.customer.remove(id);
        return Response.ok(removedCustomer).build();
    }
}