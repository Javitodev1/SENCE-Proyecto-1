package com.core;

public class Book {
    private int id;
    private String title;
    private String author;
    private float price;
    private float discount;

    public Book(int id, String title, String author, float price, float discount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.discount = discount;
    }

    public Book(String title, String author, float price, float discount) {
        this(-1, title, author, price, discount);
    }

    public Book(String title, String author, float price) {
        this(-1, title, author, price, 1.0f);
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

    public void setAuthor(String autor) {
        this.author = autor;
    }

    public float getPrice() {
        return price * discount;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

}
