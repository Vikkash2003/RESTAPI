package project.restapi.models;

public class Author {
    private int id;
    private String name;
    private String biography;

    private String bookName;

    public Author(){
    }
    public Author(int id, String name, String biography , String bookName) {
        this.id = id;
        this.name = name;
        this.biography = biography;
        this.bookName = bookName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return String.format(
                "{\"id\":%d,\"name\":\"%s\",\"biography\":\"%s\",\"book\":\"%s\"}",
                id, name, biography, bookName
        );
    }
}
