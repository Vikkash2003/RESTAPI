package project.restapi.models;

import java.util.List;

public class Cart {
    private int customerId;
    private List<CartItem> cartItems;

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
}
