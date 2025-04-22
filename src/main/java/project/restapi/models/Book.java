package project.restapi.models;

import java.util.Date;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int publishedYear;
    private Double price;
    private int stock;


    public Book(){
    }

    public Book(int id, String title, String author, String isbn, int publishedYear, Double price, int stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return String.format(
                "{\"id\":%d,\"title\":\"%s\",\"author\":\"%s\",\"isbn\":\"%s\"," +
                        "\"publishedYear\":\"%s\",\"price\":%.2f,\"stock\":%d}",
                id, title, author, isbn, publishedYear, price, stock
        );
    }
}
