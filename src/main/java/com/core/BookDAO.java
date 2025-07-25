package com.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private final Connection conn;

    public BookDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean store(Book book) throws SQLException {
        String sql = "INSERT INTO books (id, title, author, price, discount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, book.getId());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setFloat(4, book.getPrice() / book.getDiscount());
            pstmt.setFloat(5, book.getDiscount());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean update(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, price = ?, discount = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setFloat(3, book.getPrice() / book.getDiscount());
            pstmt.setFloat(4, book.getDiscount());
            pstmt.setInt(5, book.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean remove(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Book> getAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getFloat("price"),
                        rs.getFloat("discount"));
                books.add(book);
            }
        }
        return books;
    }

}
