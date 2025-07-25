# 📚 Proyecto de Librería - Portafolio TDD y Testing Ágil

Este repositorio contiene el desarrollo de un proyecto de librería usando Java, SQLite y pruebas automatizadas con TDD (Test Driven Development) como parte del portafolio de los módulos 2 y 3.

---


# 🧩 4️⃣ Integración de Teoría (Módulo 2) con Práctica (Módulo 3)

## ✅ Historia(s) de Usuario

### 🧾 HU01 – Listar libros
**Como** usuario de la librería  
**Quiero** ver todos los libros disponibles  
**Para** conocer el catálogo completo.

**Criterios de aceptación:**
- El sistema debe listar todos los libros con los campos: título, autor, precio y descuento, si aplica.
- El listado debe excluir libros que hayan sido eliminados por falta de stock.
- Se mostrarán los libros listados por orden alfabético del título.

---

### 🔍 HU02 – Buscar por título
**Como** usuario  
**Quiero** buscar un libro por su título  
**Para** encontrarlo fácilmente.

**Criterios de aceptación:**
- La búsqueda debe aceptar coincidencias parciales (e.g. “Cien” devuelve “Cien años de soledad”).
- La búsqueda debe ser insensible a mayúsculas/minúsculas.
- Si no hay resultados, debe mostrarse un mensaje indicando que no se encontraron coincidencias.

---

### 🧑‍🎓 HU03 – Filtrar por autor
**Como** lector  
**Quiero** filtrar libros por autor  
**Para** revisar obras de mi autor favorito.

**Criterios de aceptación:**
- Al ingresar el nombre del autor, se deben mostrar todos sus libros disponibles.
- El filtro debe funcionar con coincidencias parciales del nombre del autor.
- El resultado debe excluir libros sin stock del autor seleccionado.

---

### 💰 HU04 – Modificar precio aplicando descuento
**Como** administrador  
**Quiero** modificar el precio aplicando un descuento  
**Para** actualizar promociones vigentes.

**Criterios de aceptación:**
- El nuevo precio debe calcularse correctamente según el porcentaje de descuento.
- Si el descuento es 0%, el precio original no debe cambiar.
- El descuento no debe ser negativo ni superar el 100% (se debe validar el valor ingresado).

---

### 🗑️ HU05 – Eliminar libro si no hay stock
**Como** administrador  
**Quiero** eliminar un libro del sistema si ya no está en stock  
**Para** mantener la base de datos limpia y actualizada.

**Criterios de aceptación:**
- Si el stock del libro llega a 0, debe eliminarse automáticamente de la base de datos o marcarse como inactivo.
- El libro eliminado ya no debe aparecer en búsquedas ni listados.
- Debe registrarse un log o mensaje de confirmación al eliminar un libro por falta de stock.

---

## ✅ Tipos de Pruebas

- **Unitarias**: pruebas sobre cada operación individual (`createBook`, `getBooks`, etc.).
- **Integración**: pruebas conectando con la base de datos real SQLite.
- **Aceptación**: validación de criterios funcionales completos usando ejemplos de historias de usuario.

---

## ✅ Definición de “Terminado”

- Código funcional implementado con TDD (ciclo Red-Green-Refactor).
- Todas las pruebas pasan correctamente.
- Se obtiene ≥80% de cobertura con JaCoCo.
- Repositorio limpio con commits frecuentes y mensajes descriptivos.
- Refactorizaciones documentadas aplicando principios SOLID.

---

## ✅ Plan de Ejecución de Pruebas durante el Sprint

- Cada historia de usuario se abordará con su respectivo test antes del código (TDD).
- Se integrará la validación con SQLite desde el inicio (usando repositorio y JDBC).
- Se verificará la cobertura y se refactorizará tras cada prueba exitosa.
- Se revisará funcionalidad contra los criterios de aceptación.
- Se ejecutarán pruebas automáticas con `mvn test` y se documentarán los resultados.

---

## ✅ Roles Involucrados en el Sprint

| Rol           | Responsabilidad                                                  |
|----------------|------------------------------------------------------------------|
| Developer      | Implementa lógica, pruebas unitarias y conexión BD              |
| QA             | Revisa criterios, realiza plan de pruebas, ejecuta pruebas de integración |
| Revisor (par)  | Evalúa código y da feedback sobre buenas prácticas              |
| Product owner  | Define historias de usuario, revisa cumplimiento de entregables y criterios |
| Scrum master   | Facilitador para el equipo de desarrollo                         |

🧠 Reflexión Final
Durante el desarrollo del proyecto, aprendimos a aplicar los principios del Testing Ágil y a integrar el enfoque TDD (Red-Green-Refactor) para construir funcionalidades desde los tests. Este enfoque nos ayudó a tener mayor claridad sobre los requisitos y a detectar errores desde etapas tempranas.

Una de las principales dificultades fue configurar correctamente el entorno de pruebas con TestNG y simular interacciones con la base de datos sin romper la lógica de negocio. Para resolverlo, utilizamos mocks y mejoramos la estructura del código siguiendo buenas prácticas.

Trabajar con ciclos TDD fue desafiante al principio, pero con el tiempo resultó motivador y satisfactorio ver cómo las pruebas guiaban el diseño del código. Si repitiéramos el proyecto, organizaríamos mejor los paquetes desde el inicio y automatizaríamos aún más la cobertura de pruebas para mantener la calidad del software.

---

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