package project.restapi.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(int authorId) {
        super("Book not found with ID: " + authorId);
    }
}