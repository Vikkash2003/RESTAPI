package project.restapi.resourses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.restapi.models.Book;
import project.restapi.utills.DataStore;

@Path("/books")
public class BookResource {

    //creating book
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBook(Book book) {
        book.setId(DataStore.book.size() + 1);
        DataStore.book.put(book.getId(), book);
        return Response.status(Response.Status.CREATED)
                .entity(book)
                .build();
    }

    //getting all books
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBooks() {
        if (DataStore.book.size() > 0) {
            StringBuilder books = new StringBuilder("[");
            for (Book book : DataStore.book.values()) {
                books.append(book.toString()).append(",");
            }
            books.deleteCharAt(books.length() - 1); // Remove the last comma
            books.append("]");
            return books.toString();
        }
        return "[]";
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") int id, Book book) {
        if (DataStore.book.containsKey(id)) {
            book.setId(id);
            DataStore.book.put(id, book);
            return Response.ok(book).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found with ID: " + id)
                    .build();
        }
    }

    //deleting book
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("id") int id) {
        if (DataStore.book.containsKey(id)) {
            DataStore.book.remove(id);
            return Response.ok("Book with ID: " + id + " deleted successfully.").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found with ID: " + id)
                    .build();
        }
    }


    //getting book by id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") int id) {
        Book book = DataStore.book.get(id);
        if (book != null) {
            return Response.ok(book).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found with ID: " + id)
                    .build();
        }
    }



}
