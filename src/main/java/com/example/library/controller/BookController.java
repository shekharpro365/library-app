package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    private  final BookRepository bookRepository;
    private  final MessageSource messageSource;

    @PostMapping({"", "/"})
    public ResponseEntity<?> addBook(@Valid @RequestBody Book book) {
        if (book.getPrice() < 0) {
            String msg = messageSource.getMessage("BOOK_PRICE_NEGATIVE", null, LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest().body(msg);
        }
        Book savedBook = bookRepository.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @GetMapping({"", "/"})
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            String msg = messageSource.getMessage("BOOK_NOT_FOUND", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(book.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody Book updatedBook) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isEmpty()) {
            String msg = messageSource.getMessage("BOOK_NOT_FOUND", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (updatedBook.getPrice() < 0) {
            String msg = messageSource.getMessage("BOOK_PRICE_NEGATIVE", null, LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest().body(msg);
        }
        updatedBook.setId(id);
        Book savedBook = bookRepository.save(updatedBook);
        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            String msg = messageSource.getMessage("BOOK_NOT_FOUND", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    public BookController(BookRepository bookRepository, MessageSource messageSource) {
        this.bookRepository = bookRepository;
        this.messageSource = messageSource;
    }
}
