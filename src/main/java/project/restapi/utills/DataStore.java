package project.restapi.utills;

import project.restapi.models.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {

    public static Map<Integer, Book> book = new HashMap<>();

    public static Map<Integer, Customer> customer = new HashMap<>();
    public static Map<Integer, Author> author = new HashMap<>();
    public static Map<Integer, List<Order>> order = new HashMap<>(); // Note: using 'orders' instead of 'order'
    public static Map<Integer, Cart> cart = new HashMap<>();

}
