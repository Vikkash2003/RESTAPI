package project.restapi.resourses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.restapi.exceptions.AuthorNotFoundException;
import project.restapi.exceptions.InvalidInputException;
import project.restapi.models.Author;
import project.restapi.models.Book;
import project.restapi.utills.DataStore;

import java.util.*;

@Path("/author")
public class AuthorResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAuthor(Author author) {

        if (author == null) {
            throw new InvalidInputException("Author cannot be null");
        }
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new AuthorNotFoundException("Author details cannot be empty");
        }

        if (author.getBiography() == null || author.getBiography().trim().isEmpty()) {
            throw new InvalidInputException("Author biography cannot be empty");
        }

        for (Author existingAuthor : DataStore.author.values()) {
            if (existingAuthor.getName().equalsIgnoreCase(author.getName())) {
                return Response.ok()
                        .entity(Map.of("message", "Author already exists",
                                "authorId", existingAuthor.getId()))
                        .build();
            }
        }

        author.setId(DataStore.author.size() + 1);
        DataStore.author.put(author.getId(), author);
        return Response.status(Response.Status.CREATED)
                .entity(author)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthors() {
        List<Author> authors = new ArrayList<>(DataStore.author.values());
        if (authors.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of(
                            "message", "No authors found in the system",
                            "statusCode", 404
                    ))
                    .build();
        }
        return Response.ok(authors).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthorById(@PathParam("id") int id) {
        Author author = DataStore.author.get(id);
        if (author == null) {
            throw new AuthorNotFoundException(id);
        }
        return Response.ok(author).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAuthor(@PathParam("id") int id, Author author) {
        if (author == null || author.getName() == null || author.getName().trim().isEmpty()) {
            throw new AuthorNotFoundException("Author details cannot be empty");
        }

        if (!DataStore.author.containsKey(id)) {
            throw new AuthorNotFoundException(id);
        }

        author.setId(id);
        DataStore.author.put(id, author);
        return Response.ok(author).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAuthor(@PathParam("id") int id) {
        if (!DataStore.author.containsKey(id)) {
            throw new AuthorNotFoundException(id);
        }

        String authorName = DataStore.author.get(id).getName();
        for (Book book : DataStore.book.values()) {
            if (book.getAuthor().equalsIgnoreCase(authorName)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "Cannot delete author with existing books",
                                "status", 400))
                        .build();
            }
        }

        DataStore.author.remove(id);
        return Response.ok()
                .entity(Map.of("message", "Author deleted successfully"))
                .build();
    }

    @GET
    @Path("/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksAuthor(@PathParam("id") int authorId) {
        Author author = DataStore.author.get(authorId);
        if (author == null) {
            throw new AuthorNotFoundException(authorId);
        }

        List<Book> authorBooks = new ArrayList<>();
        for (Book book : DataStore.book.values()) {
            if (book.getAuthor().equalsIgnoreCase(author.getName())) {
                authorBooks.add(book);
            }
        }

        return Response.ok(authorBooks).build();
    }

}