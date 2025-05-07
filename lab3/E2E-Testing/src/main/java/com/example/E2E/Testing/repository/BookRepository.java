package com.example.E2E.Testing.repository;

import com.example.E2E.Testing.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}