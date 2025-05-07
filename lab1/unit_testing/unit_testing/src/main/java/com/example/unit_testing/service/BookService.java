package com.example.unit_testing.service;

import com.example.unit_testing.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BookService {

    private final List<Book> books = new ArrayList<>(Arrays.asList(
            new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald"),
            new Book(2L, "1984", "George Orwell")
    ));

    public List<Book> getAllBooks() {
        return books;
    }

    public Book getBookById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Book addBook(Book book) {
        return book;
    }
}
