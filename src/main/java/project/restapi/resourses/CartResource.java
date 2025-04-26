package project.restapi.resourses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.restapi.exceptions.*;
import project.restapi.models.Book;
import project.restapi.models.Cart;
import project.restapi.models.CartItem;
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

        if (customerId <= 0) {
            throw new InvalidInputException("Invalid customer ID");
        }
        if (cartItem == null) {
            throw new InvalidInputException("Cart item cannot be null");
        }
        if (cartItem.getBookId() <= 0) {
            throw new InvalidInputException("Invalid book ID");
        }
        if (cartItem.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than 0");
        }
        // Validate customer
        if (!DataStore.customer.containsKey(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        // Validate book and check stock
        Book book = DataStore.book.get(cartItem.getBookId());
        if (book == null) {
            throw new BookNotFoundException("Book not found with ID: " + cartItem.getBookId());
        }

        // Check if requested quantity is available
        if (book.getStock() < cartItem.getQuantity()) {
            throw new OutOfStockException("Insufficient stock. Available: " + book.getStock());
        }

        // Get or create cart
        Cart cart = DataStore.cart.get(customerId);
        if (cart == null) {
            cart = new Cart(customerId, new ArrayList<>());
        }

        // Check if book already in cart
        for (CartItem item : cart.getCartItems()) {
            if (item.getBookId() == cartItem.getBookId()) {
                int newQuantity = item.getQuantity() + cartItem.getQuantity();
                if (book.getStock() < cartItem.getQuantity()) {
                    throw new OutOfStockException("Insufficient stock. Available: " + book.getStock());
                }
                item.setQuantity(newQuantity);
                book.decreaseStock(cartItem.getQuantity());
                DataStore.cart.put(customerId, cart);
                return Response.ok(cart).build();
            }
        }

        // Add new item to cart and decrease stock
        book.decreaseStock(cartItem.getQuantity());
        cart.getCartItems().add(cartItem);
        DataStore.cart.put(customerId, cart);


        if (book.getStock() == 0) {
            throw new OutOfStockException(cartItem.getBookId());
        }

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
            throw new CustomerNotFoundException(customerId);
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
            throw new CustomerNotFoundException(customerId);
        }

        // Validate book exists in store
        Book book = DataStore.book.get(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book not found with ID: " + bookId);
        }

        // Validate cart exists
        Cart cart = DataStore.cart.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for customer ID: " + customerId);
        }

        // Find and update book in cart
        boolean bookFound = false;
        for (CartItem item : cart.getCartItems()) {
            if (item.getBookId() == bookId) {
                bookFound = true;
                int quantityDiff = updatedItem.getQuantity() - item.getQuantity();

                // Check stock before updating
                if (quantityDiff > 0 && book.getStock() < quantityDiff) {
                    throw new OutOfStockException("Insufficient stock. Available: " + book.getStock());
                }

                // Update stock
                if (quantityDiff > 0) {
                    book.decreaseStock(quantityDiff);
                } else {
                    book.setStock(book.getStock() - quantityDiff);
                }

                item.setQuantity(updatedItem.getQuantity());
                DataStore.cart.put(customerId, cart);
                return Response.ok(cart).build();
            }
        }

        if (!bookFound) {
            throw new BookNotFoundException("Book not found in cart with ID: " + bookId);
        }

        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFromCart(@PathParam("customerId") int customerId,
                                   @PathParam("bookId") int bookId) {
        // Validate customer
        if (!DataStore.customer.containsKey(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        Cart cart = DataStore.cart.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for customer ID: " + customerId);
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

        throw new BookNotFoundException("Book not found in cart with ID: " + bookId);
    }
}