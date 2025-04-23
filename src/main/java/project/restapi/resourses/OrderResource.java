package project.restapi.resourses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
        // Validate customer
        if (!DataStore.customer.containsKey(customerId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Customer not found with ID: " + customerId)
                    .build();
        }

        // Get cart
        Cart cart = DataStore.cart.get(customerId);
        if (cart == null || cart.getCartItems().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Cart is empty")
                    .build();
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
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Customer not found with ID: " + customerId)
                    .build();
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
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Customer not found with ID: " + customerId)
                    .build();
        }

        List<Order> orders = DataStore.order.getOrDefault(customerId, new ArrayList<>());
        return orders.stream()
                .filter(order -> order.getId() == orderId)
                .findFirst()
                .map(order -> Response.ok(order).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Order not found with ID: " + orderId)
                        .build());
    }
}