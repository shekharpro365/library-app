package com.example.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/books")
public class BookController {

    private final Map<Long, Book> bookRepository = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();
    private final MessageSource messageSource;

    @Autowired
    public BookController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @PostMapping({"", "/"})
    public ResponseEntity<?> addBook(@Valid @RequestBody Book book) {
        if (book.getPrice() < 0) {
            String msg = messageSource.getMessage("BOOK_PRICE_NEGATIVE", null, LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest().body(msg);
        }
        long id = idCounter.incrementAndGet();
        book.setId(id);
        bookRepository.put(id, book);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @GetMapping({"", "/"})
    public Collection<Book> getAllBooks() {
        return bookRepository.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        Book book = bookRepository.get(id);
        if (book == null) {

            String msg = messageSource.getMessage("BOOK_NOT_FOUND", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody Book updatedBook) {
        Book existingBook = bookRepository.get(id);
        if (existingBook == null) {
            String msg = messageSource.getMessage("BOOK_NOT_FOUND", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (updatedBook.getPrice() < 0) {
            String msg = messageSource.getMessage("BOOK_PRICE_NEGATIVE", null, LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest().body(msg);
        }
        updatedBook.setId(id);
        bookRepository.put(id, updatedBook);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        Book removedBook = bookRepository.remove(id);
        if (removedBook == null) {
            String msg = messageSource.getMessage("BOOK_NOT_FOUND", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.noContent().build();
    }
}
