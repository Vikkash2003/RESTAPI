package project.restapi.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }
    public CartNotFoundException(int cartId) {
        super("Cart not found with ID: " + cartId);
    }
}