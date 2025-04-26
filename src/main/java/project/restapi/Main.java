package project.restapi;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import project.restapi.mapper.AuthorNotFoundExceptionMapper;
import project.restapi.mapper.BookNotFoundExceptionMapper;
import project.restapi.mapper.CartNotFoundExceptionMapper;
import project.restapi.mapper.CustomerNotFoundExceptionMapper;
import project.restapi.resourses.*;
import project.restapi.mapper.*;


import java.io.IOException;
import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://localhost:8080/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig()
                .packages("project.restapi.resourses" ,
                        "project.restapi.mapper")
                .register(BookResource.class)
                .register(AuthorResource.class)
                .register(CustomerResource.class)
                .register(CartResource.class)
                .register(OrderResource.class)
                .register(BookNotFoundExceptionMapper.class)
                .register(AuthorNotFoundExceptionMapper.class)
                .register(CustomerNotFoundExceptionMapper.class)
                .register(CartNotFoundExceptionMapper.class)
                .register(OutOfStockExceptionMapper.class)
                .register(EmptyCartExceptionMapper.class)
                .register(OutOfStockExceptionMapper.class)
                .register(InvalidInputExceptionMapper.class)
                .register(org.glassfish.jersey.jackson.JacksonFeature.class);
        rc.register(org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider.class);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println("Jersey app started at " + BASE_URI);
        System.in.read();
        server.stop();
    }
}