package project.restapi.utills;

import project.restapi.models.*;

import java.util.HashMap;
import java.util.Map;

public class DataStore {

    public static Map<Integer, Book> book = new HashMap<>();

    public static Map<Integer, Customer> customer = new HashMap<>();
    public static Map<Integer, Author> author = new HashMap<>();
    public static Map<Integer, Order> order = new HashMap<>();
    public static Map<Integer, Cart> cart = new HashMap<>();

}
