package com.example.library;

import com.example.library.model.Book;
import com.example.library.controller.BookController;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BookControllerUnitTests {

    private BookController controller;
    private BookRepository bookRepository;
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        bookRepository = mock(BookRepository.class);
        messageSource = mock(MessageSource.class);

        // Mock messageSource for not found and price validation
        when(messageSource.getMessage(eq("BOOK_NOT_FOUND"), any(), any(Locale.class)))
                .thenReturn("Book not found");
        when(messageSource.getMessage(eq("BOOK_PRICE_NEGATIVE"), any(), any(Locale.class)))
                .thenReturn("Price must be non-negative");

        // Instantiate controller with mocks
        controller = new BookController(bookRepository, messageSource);
    }

    @Test
    void testAddBook() {
        Book book = new Book();
        book.setTitle("Think and Grow Rich");
        book.setAuthor("Napoleon Hill");
        book.setPrice(100.0);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle(book.getTitle());
        savedBook.setAuthor(book.getAuthor());
        savedBook.setPrice(book.getPrice());

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

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
    void testAddBook_NegativePrice() {
        Book book = new Book();
        book.setTitle("Test");
        book.setAuthor("Test Author");
        book.setPrice(-10.0);

        ResponseEntity<?> response = controller.addBook(book);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Price must be non-negative", response.getBody());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(9000L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getBookById(9000L);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Book not found", response.getBody());
    }

    @Test
    void testGetBookById_Found() {
        Book book = new Book();
        book.setId(2L);
        book.setTitle("Test");
        book.setAuthor("Test Author");
        book.setPrice(12.0);

        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));

        ResponseEntity<?> response = controller.getBookById(2L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(book, response.getBody());
    }
}
