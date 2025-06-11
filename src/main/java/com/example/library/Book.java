package com.example.library;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

public class Book {
    private Long id;

    @NotBlank(message = "{BOOK_TITLE_MANDATORY}")
    private String title;

    @NotBlank(message = "{BOOK_AUTHOR_MANDATORY}")
    private String author;

    @Min(value = 0, message = "{BOOK_PRICE_NEGATIVE}")
    private double price;

    public Book() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}