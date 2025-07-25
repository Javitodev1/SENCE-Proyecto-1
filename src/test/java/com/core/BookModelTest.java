package com.core;

import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookModelTest {

    private BookRepository repository;
    private BookModel model;

    @BeforeMethod
public void setUp() {
    repository = mock(BookRepository.class);
    when(repository.getBooks()).thenReturn(
        new java.util.ArrayList<>(Arrays.asList(
            new Book("Cien años de soledad", "Gabriel García Márquez", 12000),
            new Book("El túnel", "Ernesto Sabato", 8000)
        ))
    );

    model = new BookModel(repository);
}


    @Test
    public void testCreateBook_ShouldStoreBook() {
        when(repository.storeBook(any())).thenReturn(true);

        Book created = model.createBook("Rayuela", "Julio Cortázar", 10000);

        assertEquals(created.getTitle(), "Rayuela");
        assertEquals(created.getAuthor(), "Julio Cortázar");
        verify(repository).storeBook(any(Book.class));
    }

    @Test
    public void testFindBookByTitle_ShouldReturnCorrectBook() {
        Book result = model.findBookByTitle("El túnel");

        assertNotNull(result);
        assertEquals(result.getAuthor(), "Ernesto Sabato");
    }

    @Test
    public void testFilterByAuthor_ShouldReturnMatchingBooks() {
        List<Book> filtered = model.filterByAuthor("Gabriel García Márquez");

        assertEquals(filtered.size(), 1);
        assertEquals(filtered.get(0).getTitle(), "Cien años de soledad");
    }

    @Test
    public void testFilterByPrice_ShouldReturnCheaperBooks() {
        List<Book> result = model.filterByPrice(9000);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getTitle(), "El túnel");
    }

    @Test
    public void testApplyDiscount_ShouldUpdateBook() {
        Book book = model.findBookByTitle("El túnel");
        book.setId(2);
        when(repository.updateBook(any())).thenReturn(true);

        Book discounted = model.applyDiscount(2, 15.0f);

        assertNotNull(discounted);
        assertEquals(discounted.getDiscount(), 15.0f);
        verify(repository).updateBook(book);
    }

    @Test
    public void testRemoveById_ShouldRemoveBookIfExists() {
        Book book = model.findBookByTitle("Cien años de soledad");
        book.setId(1);
        when(repository.removeById(1)).thenReturn(true);

        boolean result = model.removeById(1);

        assertTrue(result);
        verify(repository).removeById(1);
    }
}
