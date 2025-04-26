package project.restapi.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(int customerId) {
        super("Customer not found with ID: " + customerId);
    }
}