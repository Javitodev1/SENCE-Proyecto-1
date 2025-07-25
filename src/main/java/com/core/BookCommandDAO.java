package com.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookCommandDAO {
    private final Connection conn;

    public BookCommandDAO(Connection conn) {
        this.conn = conn;
    }

    public void createTable() throws SQLException {
        String sql = SqlStatementBuilder.createTable();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public boolean store(Book book) throws SQLException {
        String sql = SqlStatementBuilder.insertBook();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setFloat(3, book.getPrice() / book.getDiscount());
            pstmt.setFloat(4, book.getDiscount());

            int affected = pstmt.executeUpdate();

            if (affected == 0) {
                return false;
            }
            
            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {
                book.setId(keys.getInt(1));
                return true;
            }
        }

        return false;
    }

    public boolean update(Book book) throws SQLException {
        String sql = SqlStatementBuilder.updateBook();
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
        String sql = SqlStatementBuilder.deleteBook();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Book> getAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = SqlStatementBuilder.selectAllBooks();
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
