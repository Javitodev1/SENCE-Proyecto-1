package com.core;

public class SqlStatementBuilder {

    public static String createTable() {
        return "CREATE TABLE IF NOT EXISTS books ( id INTEGER PRIMARY KEY, title TEXT NOT NULL, author TEXT NOT NULL, price REAL NOT NULL, discount REAL NOT NULL );";
    }

    public static String insertBook() {
        return "INSERT INTO books (title, author, price, discount) VALUES (?, ?, ?, ?)";
    }

    public static String updateBook() {
        return "UPDATE books SET title = ?, author = ?, price = ?, discount = ? WHERE id = ?";
    }

    public static String deleteBook() {
        return "DELETE FROM books WHERE id = ?";
    }

    public static String selectAllBooks() {
        return "SELECT * FROM books";
    }
}
