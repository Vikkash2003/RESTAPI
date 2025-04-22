package project.restapi.resourses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.restapi.models.Author;
import project.restapi.models.Book;
import project.restapi.utills.DataStore;



@Path("/author")
public class AuthorResource {

    //add an author
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAuthor(Author author) {
        author.setId(DataStore.author.size() + 1);
        DataStore.author.put(author.getId(),author);
        return Response.status(Response.Status.CREATED)
                .entity(author)
                .build();
    }

    //getting all authors
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAuthors() {
        if (DataStore.author.size() > 0) {
            StringBuilder authors = new StringBuilder("[");
            for (Author author : DataStore.author.values()) {
                authors.append(author.toString()).append(",");
            }
            authors.deleteCharAt(authors.length() - 1); // Remove the last comma
            authors.append("]");
            return authors.toString();
        }
        return "[]";
    }

    //getting author by id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthorById(@PathParam("id") int id) {
        Author author = DataStore.author.get(id);
        if (author != null) {
            return Response.ok(author).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Author not found with ID: " + id)
                    .build();
        }
    }

    //Update author details
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAuthor(@PathParam("id") int id, Author author) {
        if (DataStore.author.containsKey(id)) {
            author.setId(id);
            DataStore.author.put(id, author);
            return Response.ok(author).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Author not found with ID: " + id)
                    .build();
        }
    }

    //deleting author
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAuthor(@PathParam("id") int id) {
        if (DataStore.author.containsKey(id)) {
            DataStore.author.remove(id);
            return Response.ok("Author with ID: " + id + " deleted successfully.").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Author not found with ID: " + id)
                    .build();
        }
    }

    //get author based books
    @GET
    @Path("/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksAuthor(@PathParam("id") int authorId) {
        Author author = DataStore.author.get(authorId);
        if (author != null) {
            StringBuilder books = new StringBuilder("[");
            boolean hasBooks = false;

            for (Book book : DataStore.book.values()) {
                if (book.getAuthor().equalsIgnoreCase(author.getName()) &&
                        book.getTitle().equalsIgnoreCase(author.getBookName())) {
                    if (hasBooks) {
                        books.append(",");
                    }
                    books.append(book.toString());
                    hasBooks = true;
                }
            }
            books.append("]");

            return Response.ok(hasBooks ? books.toString() : "[]").build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("Author not found with ID: " + authorId)
                .build();
    }
}
