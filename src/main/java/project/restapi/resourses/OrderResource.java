package project.restapi.resourses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.restapi.exceptions.CustomerNotFoundException;
import project.restapi.exceptions.EmptyCartException;
import project.restapi.exceptions.InvalidInputException;
import project.restapi.exceptions.OrderNotFoundException;
import project.restapi.models.*;
import project.restapi.utills.DataStore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/customers/{customerId}/orders")
public class OrderResource {
    private static int nextId = 1;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrder(@PathParam("customerId") int customerId) {

        if (customerId <= 0) {
            throw new InvalidInputException("Invalid customer ID");
        }

        if (!DataStore.customer.containsKey(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        Cart cart = DataStore.cart.get(customerId);
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new EmptyCartException("Cart is empty");
        }

        // Validate stock and calculate total
        double totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            Book book = DataStore.book.get(cartItem.getBookId());
            if (book == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Book not found with ID: " + cartItem.getBookId())
                        .build();
            }

            if (book.getStock() < cartItem.getQuantity()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Insufficient stock for book: " + book.getTitle())
                        .build();
            }

            // Create order item
            OrderItem orderItem = new OrderItem(
                    cartItem.getBookId(),
                    cartItem.getQuantity(),
                    book.getPrice(),
                    book.getTitle()
            );
            orderItems.add(orderItem);

            // Calculate total and update stock
            totalAmount += book.getPrice() * cartItem.getQuantity();
            book.setStock(book.getStock() - cartItem.getQuantity());
        }

        // Create order with current date
        String orderDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Order order = new Order(nextId++, customerId, orderItems, orderDate, totalAmount);

        // Save order
        List<Order> customerOrders = DataStore.order.computeIfAbsent(customerId, k -> new ArrayList<>());
        customerOrders.add(order);

        // Clear cart
        DataStore.cart.remove(customerId);

        return Response.status(Response.Status.CREATED)
                .entity(order)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerOrders(@PathParam("customerId") int customerId) {
        if (!DataStore.customer.containsKey(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        List<Order> orders = DataStore.order.getOrDefault(customerId, new ArrayList<>());
        double totalAmount = orders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        return Response.ok()
                .entity(Map.of(
                        "orders", orders,
                        "totalOrdersAmount", totalAmount
                ))
                .build();
    }

    @GET
    @Path("/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrder(@PathParam("customerId") int customerId,
                             @PathParam("orderId") int orderId) {
        if (!DataStore.customer.containsKey(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        List<Order> orders = DataStore.order.getOrDefault(customerId, new ArrayList<>());
        return orders.stream()
                .filter(order -> order.getId() == orderId)
                .findFirst()
                .map(order -> Response.ok(order).build())
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}