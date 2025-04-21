package models;

public class CartItem {
    private int BookId;
    private int quantity;

    public CartItem(int BookId, int quantity) {
        this.BookId = BookId;
        this.quantity = quantity;
    }
    public int getBookId() {
        return BookId;
    }

    public void setBookId(int BookId) {
        this.BookId = BookId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
