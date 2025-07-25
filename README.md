# SENCE-Proyecto-1
Crear un app con operaciones CRUD y base de datos sql

## Refactorizacion del código

En la clase SqliteBookRepository se mezcla la logica de los operadores SQL,
la logica para realizar la operacion, y la logica del manejo de errores y adaptacion con el modelo.

```java
// SqliteBookRepository.java
package com.core;

import java.sql.*;
import java.util.*;

public class SqliteBookRepository implements BookRepository {
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
            
            int affected = pstmt.executeUpdate();

            if (affected == 0) {
                return false;
            }

            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {
                book.setId(keys.getInt(1));
                return true;
            }

            return false;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }
    // ... more code
}

```

Para una separación efectiva de responsabilidades siguiendo el principio SOLID de responsabilidad única, separaremos las query SQL en una clase SqlStatementBuilder y las operaciones SQL en un Database Access Object, lo cual es una practica común en bases de datos:

```java
// SqlStatementBuilder.java
package com.core;

public class SqlStatementBuilder {

    public static String createTable() {
        return "CREATE TABLE IF NOT EXISTS books ( id INTEGER PRIMARY KEY, title TEXT NOT NULL, author TEXT NOT NULL, price REAL NOT NULL, discount REAL NOT NULL );";
    }

    public static String insertBook() {
        return "INSERT INTO books (title, author, price, discount) VALUES (?, ?, ?, ?)";
    }
    // ... more code
}
```

```java
// BookCommandDAO.java
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
        Statement stmt = conn.createStatement();

        stmt.execute(sql);
    }

    public boolean store(Book book) throws SQLException {
        String sql = SqlStatementBuilder.insertBook();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

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

        return false;
    }
    // ... more code
}
```

Con estas dos clases, refactorizamos SqliteBookRepository

```java
// SqliteBookRepository
package com.core;

import java.sql.*;
import java.util.*;

public class SqliteBookRepository implements BookRepository {
    private final String dbPath;
    private final boolean inMemory;
    private Connection conn;
    private BookCommandDAO commandDAO;

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

    // ... more code
}
```

De esta forma separamos efectivamente las responsabilidades, mejorando la legibilidad del código y su facilitando su mantenimiento, el resto del codigo puede ser encontrado en el repositorio.