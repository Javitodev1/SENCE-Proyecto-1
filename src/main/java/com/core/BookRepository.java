package com.core;

import java.util.List;

public interface BookRepository {
    boolean storeBook(Book book);

    boolean updateBook(Book book);
    
    boolean removeById(int id);

    List<Book> getBooks();
}
