package com.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookModel {
    BookRepository repository;
    List<Book> books;

    public BookModel(BookRepository repository) {
    this.repository = repository;
    // SOLUCIÓN: hacer una copia mutable de la lista
    this.books = new ArrayList<>(repository.getBooks());
}


    public Book createBook(String title, String autor, float price) {
        Book newBook = new Book(title, autor, price);
        books.add(newBook);

        boolean success = repository.storeBook(newBook);
        if (!success) {
            LoggingService.log("Book with title: " + title + " could not be stored into database.");
        }

        return newBook;
    }

    public Book findBookById(int id) {
        Book targetBook = books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);

        if (targetBook == null) {
            LoggingService.log("Not book with id " + id + " found.");
        }

        return targetBook;
    }

    public Book applyDiscount(int id, float discount) {
        Book targetBook = findBookById(id);

        if (targetBook == null) {
            return targetBook;
        }

        targetBook.setDiscount(discount);
        repository.updateBook(targetBook);

        return targetBook;
    }

    public boolean removeById(int id) {
        Book targetBook = findBookById(id);

        if (targetBook == null) {
            return false;
        }

        boolean success = repository.removeById(id);

        if (!success) {
            LoggingService.log("Book with id: " + id + " could not be removed from database.");
        }

        return success;
    }

    public List<Book> getBooks() {
        return books;
    }

    public Book findBookByTitle(String title) {
        Book targetBook = books.stream()
                .filter(book -> book.getTitle() == title)
                .findFirst()
                .orElse(null);

        if (targetBook == null) {
            LoggingService.log("Not book with title " + title + " found.");
        }

        return targetBook;
    }

    public List<Book> filterByPrice(float price) {
        return books.stream()
                .filter(book -> book.getPrice() <= price)
                .collect(Collectors.toList());
    }

    public List<Book> filterByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().equals(author))
                .collect(Collectors.toList());
    }
}
