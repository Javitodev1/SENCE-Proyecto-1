package com.core;

import java.sql.*;
import java.util.*;

public class SqliteBookRepository implements BookRepository {
    private final String dbPath;
    private final boolean inMemory;

    public SqliteBookRepository(String dbPath, boolean inMemory) {
        this.dbPath = dbPath;
        this.inMemory = inMemory;
        initTable();
    }

    public SqliteBookRepository(String dbPath) {
        this(dbPath, false); // default: persistent
    }

    private Connection connect() throws SQLException {
        String url = inMemory ? "jdbc:sqlite::memory:" : "jdbc:sqlite:" + dbPath;
        return DriverManager.getConnection(url);
    }

    private void initTable() {
        String sql = "CREATE TABLE IF NOT EXISTS books ( id INTEGER PRIMARY KEY, title TEXT NOT NULL, author TEXT NOT NULL, price REAL NOT NULL, discount REAL NOT NULL );";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    @Override
    public boolean storeBook(Book book) {
        String sql = "INSERT INTO books (id, title, author, price, discount) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, book.getId());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setFloat(4, book.getPrice() / book.getDiscount()); // store original price
            pstmt.setFloat(5, book.getDiscount());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, price = ?, discount = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setFloat(3, book.getPrice() / book.getDiscount());
            pstmt.setFloat(4, book.getDiscount());
            pstmt.setInt(5, book.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeById(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getFloat("price"),
                        rs.getFloat("discount"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}
