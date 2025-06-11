package com.example.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerUnitTests {

    private BookController controller;


    @BeforeEach
    void setUp() {
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage(eq("BOOK_NOT_FOUND"), any(), any(Locale.class)))
                .thenReturn("Book not found");
        controller = new BookController(messageSource);
    }

    @Test
    void testAddBook() {
        Book book = new Book();
        book.setTitle("Think and Grow Rich");
        book.setAuthor("Napoleon Hill");
        book.setPrice(100.0);
        ResponseEntity<?> response = controller.addBook(book);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Book);
        Book created = (Book) response.getBody();
        assertEquals("Think and Grow Rich", created.getTitle());
        assertEquals("Napoleon Hill", created.getAuthor());
        assertEquals(100.0, created.getPrice());
        assertNotNull(created.getId());
    }

    @Test
    void testGetBookById_NotFound() {
        ResponseEntity<?> response = controller.getBookById(9000L);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Book not found", response.getBody());
    }
}
