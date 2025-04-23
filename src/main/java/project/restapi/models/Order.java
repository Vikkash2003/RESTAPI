package project.restapi.models;

import java.util.List;

public class Order {
    private int orderId;
    private int customerId;
    private List<OrderItem> orderItems;
    private String orderDate;
    private Double totalAmount;

    public Order(){

    }
    public Order(int orderId, int customerId, List<OrderItem> orderItems, String orderDate, Double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public int getId() {
        return orderId;
    }

    public void setId(int id) {
        this.orderId = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + orderId +
                ", customerId=" + customerId +
                ", orderItems=" + orderItems +
                ", orderDate='" + orderDate + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
