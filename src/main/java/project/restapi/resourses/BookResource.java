package project.restapi.resourses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.restapi.exceptions.BookNotFoundException;
import project.restapi.exceptions.InvalidInputException;
import project.restapi.models.Book;
import project.restapi.utills.DataStore;
import java.util.*;

@Path("/books")
public class BookResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBook(Book book) {
        if (book == null) {
            throw new InvalidInputException("Book details cannot be empty");
        }

        // Title validation
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title is required");
        }

        // Author validation
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new InvalidInputException("Author name is required");
        }

        // ISBN validation
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new InvalidInputException("ISBN is required");
        }

        // Published year validation
        if (book.getPublishedYear() <= 0) {
            throw new InvalidInputException("Published year is required and must be valid");
        }

        // Price validation
        if (book.getPrice() <= 0) {
            throw new InvalidInputException("Price must be greater than 0");
        }

        // Stock validation
        if (book.getStock() < 0) {
            throw new InvalidInputException("Stock cannot be negative");
        }

        book.setId(DataStore.book.size() + 1);
        DataStore.book.put(book.getId(), book);
        return Response.status(Response.Status.CREATED)
                .entity(book)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks() {
        List<Book> books = new ArrayList<>(DataStore.book.values());
        if (books.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of(
                            "message", "No books found in the system",
                            "statusCode", 404
                    ))
                    .build();
        }
        return Response.ok(books).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") int id) {
        Book book = DataStore.book.get(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") int id, Book book) {
        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new BookNotFoundException("Book details cannot be empty");
        }

        if (!DataStore.book.containsKey(id)) {
            throw new BookNotFoundException(id);
        }

        book.setId(id);
        DataStore.book.put(id, book);
        return Response.ok(book).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("id") int id) {
        if (!DataStore.book.containsKey(id)) {
            throw new BookNotFoundException(id);
        }

        DataStore.book.remove(id);
        return Response.ok()
                .entity(Map.of("message", "Book deleted successfully"))
                .build();
    }
}