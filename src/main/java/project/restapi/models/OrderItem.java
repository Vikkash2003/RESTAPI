package project.restapi.models;

public class OrderItem {
    private int bookId;
    private int quantity;
    private double price;
    private String bookTitle;

    private double subTotal;

    public OrderItem() {
    }

    public OrderItem(int bookId, int quantity, double price, String bookTitle) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
        this.bookTitle = bookTitle;
        this.subTotal = price * quantity;
    }

    // Getters and setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public double getSubtotal() {
        return subTotal;
    }

    public void setSubtotal(double subtotal) {
        this.subTotal = subtotal;
    }
}