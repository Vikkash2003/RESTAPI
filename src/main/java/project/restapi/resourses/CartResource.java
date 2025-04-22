package project.restapi.resourses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.restapi.models.Book;
import project.restapi.models.Cart;
import project.restapi.models.CartItem;
import project.restapi.models.Customer;
import project.restapi.utills.DataStore;

import java.util.ArrayList;
import java.util.List;

@Path("/customers/{customerId}/cart")
public class CartResource {

    //add new item to cart
    @POST
    @Path("/items")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addToCart(@PathParam("customerId") int customerId, CartItem cartItem) {
        // Validate customer
        if (!DataStore.customer.containsKey(customerId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Customer not found with ID: " + customerId)
                    .build();
        }

        // Validate book
        if (!DataStore.book.containsKey(cartItem.getBookId())) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found with ID: " + cartItem.getBookId())
                    .build();
        }

        // Get or create cart
        Cart cart = DataStore.cart.get(customerId);
        if (cart == null) {
            cart = new Cart(customerId, new ArrayList<>());
        }

        // Check if book already in cart
        for (CartItem item : cart.getCartItems()) {
            if (item.getBookId() == cartItem.getBookId()) {
                item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                DataStore.cart.put(customerId, cart);
                return Response.ok(cart).build();
            }
        }

        // Add new item to cart
        cart.getCartItems().add(cartItem);
        DataStore.cart.put(customerId, cart);
        return Response.status(Response.Status.CREATED)
                .entity(cart)
                .build();
    }

    //getCustomerCartById
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCart(@PathParam("customerId") int customerId) {
        // Validate customer
        if (!DataStore.customer.containsKey(customerId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Customer not found with ID: " + customerId)
                    .build();
        }

        Cart cart = DataStore.cart.get(customerId);
        if (cart == null) {
            cart = new Cart(customerId, new ArrayList<>());
        }

        return Response.ok(cart).build();
    }


    @PUT
    @Path("/items/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCartItem(@PathParam("customerId") int customerId,
                                   @PathParam("bookId") int bookId,
                                   CartItem updatedItem) {
        // Validate customer
        if (!DataStore.customer.containsKey(customerId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Customer not found with ID: " + customerId)
                    .build();
        }

        Cart cart = DataStore.cart.get(customerId);
        if (cart == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cart not found for customer ID: " + customerId)
                    .build();
        }

        // Update quantity
        for (CartItem item : cart.getCartItems()) {
            if (item.getBookId() == bookId) {
                item.setQuantity(updatedItem.getQuantity());
                DataStore.cart.put(customerId, cart);
                return Response.ok(cart).build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("Book not found in cart")
                .build();
    }

    @DELETE
    @Path("/items/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFromCart(@PathParam("customerId") int customerId,
                                   @PathParam("bookId") int bookId) {
        // Validate customer
        if (!DataStore.customer.containsKey(customerId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Customer not found with ID: " + customerId)
                    .build();
        }

        Cart cart = DataStore.cart.get(customerId);
        if (cart == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cart not found for customer ID: " + customerId)
                    .build();
        }

        // Remove item
        List<CartItem> items = cart.getCartItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getBookId() == bookId) {
                items.remove(i);
                DataStore.cart.put(customerId, cart);
                return Response.ok(cart).build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("Book not found in cart")
                .build();
    }
}