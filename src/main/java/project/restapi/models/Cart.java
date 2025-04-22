package project.restapi.models;

import java.util.List;

public class Cart {
    private int customerId;
    private List<CartItem> cartItems;

    public Cart() {}
    public Cart(int customerId, List<CartItem> cartItems) {
        this.customerId = customerId;
        this.cartItems = cartItems;
    }

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public List<CartItem> getCartItems() {
        return cartItems;
    }
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    // In Cart.java
    @Override
    public String toString() {
        StringBuilder cartItems = new StringBuilder();
        for (int i = 0; i < this.cartItems.size(); i++) {
            cartItems.append(this.cartItems.get(i).toString());
            if (i < this.cartItems.size() - 1) {
                cartItems.append(",");
            }
        }
        return String.format(
                "{\"customerId\":%d,\"cartItems\":[%s]}",
                customerId, cartItems.toString()
        );
    }
}
