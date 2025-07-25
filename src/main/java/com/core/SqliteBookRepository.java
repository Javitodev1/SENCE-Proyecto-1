package com.core;

import java.sql.*;
import java.util.*;

public class SqliteBookRepository implements BookRepository {
    private final String dbPath;
    private final boolean inMemory;
    private Connection conn;
    private BookCommandDAO commandDAO;

    public SqliteBookRepository(String dbPath, boolean inMemory) {
        this.dbPath = dbPath;
        this.inMemory = inMemory;
        initConnection();
        initTable();
    }

    public SqliteBookRepository(String dbPath) {
        this(dbPath, false); // default: persistent
    }

    private void initConnection() {
        try {
            String url = inMemory ? "jdbc:sqlite::memory:" : "jdbc:sqlite:" + dbPath;
            conn = DriverManager.getConnection(url);
            commandDAO = new BookCommandDAO(conn);
        } catch (SQLException e) {
            LoggingService.log("Failed to connect to database");
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    LoggingService.log("Connection closed successfully.");
                }
            } catch (SQLException e) {
                LoggingService.log("Failed to close database connection.");
                throw new RuntimeException(e);
            }
        }
    }

    private void initTable() {
        try {
            commandDAO.createTable();
        } catch (SQLException e) {
            LoggingService.log("Failed to initialize database");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean storeBook(Book book) {
        try {
            return commandDAO.store(book);
        } catch (SQLException e) {
            LoggingService.log(e.getStackTrace().toString());
            return false;
        }
    }

    @Override
    public boolean updateBook(Book book) {
        try {
            return commandDAO.update(book);
        } catch (SQLException e) {
            LoggingService.log(e.getStackTrace().toString());
            return false;
        }
    }

    @Override
    public boolean removeById(int id) {
        try {
            return commandDAO.remove(id);
        } catch (SQLException e) {
            LoggingService.log(e.getStackTrace().toString());
            return false;
        }
    }

    @Override
    public List<Book> getBooks() {
        try {
            return commandDAO.getAll();
        } catch (SQLException e) {
            LoggingService.log(e.getStackTrace().toString());
        }
        return Collections.emptyList();
    }
}
