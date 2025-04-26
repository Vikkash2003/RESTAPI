package project.restapi.exceptions;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
    public OutOfStockException(int productId) {
        super("Product with ID " + productId + " is out of stock.");
    }
}