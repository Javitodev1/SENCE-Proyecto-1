package com.core;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SqliteBookRepositoryTest {
    SqliteBookRepository repository;

    @BeforeMethod
    public void setup() {
        repository = new SqliteBookRepository(null, true);
    }

    @AfterMethod
    public void teardown() {
        repository.closeConnection();
    }

    @Test
    public void storeBookTest_shouldBeSuccessfull() {
        boolean sucess = repository.storeBook(new Book("Harry Potter", "J.K. Rowling", 9000f));
        assertTrue(sucess);

        List<Book> books = repository.getBooks();
        assertEquals(books.size(), 1);

        Book storedBook = books.get(0);
        assertEquals(storedBook.getTitle(), "Harry Potter");
        assertEquals(storedBook.getAuthor(), "J.K. Rowling");
        assertEquals(storedBook.getPrice(), 9000f);
        assertTrue(storedBook.getId() >= 0);
    }

    @Test
    public void updateBook_shouldUpdateBookSuccessfully() {
        Book book = new Book("Original", "Unknown", 100f);
        repository.storeBook(book);

        book.setTitle("Updated Title");
        book.setAuthor("Updated Author");
        book.setPrice(150f);

        boolean updated = repository.updateBook(book);
        assertTrue(updated);

        Book updatedBook = repository.getBooks().get(0);
        assertEquals(updatedBook.getTitle(), "Updated Title");
        assertEquals(updatedBook.getAuthor(), "Updated Author");
        assertEquals(updatedBook.getPrice(), 150f);
    }

    @Test
    public void removeById_shouldRemoveBookSuccessfully() {
        Book book = new Book("RemoveMe", "NoOne", 50f);
        repository.storeBook(book);

        int idToRemove = repository.getBooks().get(0).getId();
        boolean removed = repository.removeById(idToRemove);
        assertTrue(removed);

        List<Book> books = repository.getBooks();
        assertTrue(books.isEmpty(), "Book list should be empty after removal");
    }

    @Test
    public void getBooks_shouldReturnAllStoredBooks() {
        repository.storeBook(new Book("Title 1", "Author A", 11f));
        repository.storeBook(new Book("Title 2", "Author B", 22f));

        List<Book> books = repository.getBooks();
        assertEquals(books.size(), 2);
    }
}
