package com.example.integration_test;

import com.example.integration_test.model.Book;
import com.example.integration_test.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        bookRepository.save(new Book(null, "The Great Gatsby", "F. Scott Fitzgerald"));
        bookRepository.save(new Book(null, "1984", "George Orwell"));
    }

    @Test
    public void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("The Great Gatsby")))
                .andExpect(jsonPath("$[1].title", is("1984")));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = bookRepository.findAll().get(0);

        mockMvc.perform(get("/api/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(book.getTitle())));
    }

    @Test
    public void testCreateBook() throws Exception {
        Book newBook = new Book(null, "To Kill a Mockingbird", "Harper Lee");

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("To Kill a Mockingbird")))
                .andExpect(jsonPath("$.author", is("Harper Lee")));
    }

    @Test
    public void testUpdateBook() throws Exception {
        // Arrange: Get an existing book
        Book book = bookRepository.findAll().get(0);

        // Prepare updated data
        Book updatedBook = new Book(null, "Updated Title", "Updated Author");

        // Act & Assert
        mockMvc.perform(put("/api/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.author", is("Updated Author")));

        // Verify from DB
        Book savedBook = bookRepository.findById(book.getId()).orElse(null);
        assert savedBook != null;
        assert "Updated Title".equals(savedBook.getTitle());
        assert "Updated Author".equals(savedBook.getAuthor());
}

}