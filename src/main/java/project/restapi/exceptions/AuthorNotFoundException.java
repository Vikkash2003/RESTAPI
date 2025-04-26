package project.restapi.exceptions;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }

    public AuthorNotFoundException(int authorId) {
        super("Author not found with ID: " + authorId);
    }
}