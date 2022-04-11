package com.vkobinski.bookshelf.controller;

import com.vkobinski.bookshelf.model.Author;
import com.vkobinski.bookshelf.model.Book;
import com.vkobinski.bookshelf.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable(name = "id") Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable(name = "id") Long id,
                                                   @RequestParam(required = false) String name,
                                                   @RequestParam(required = false) Long authorId,
                                                   @RequestParam(required = false) String imagePath,
                                                   @RequestParam(required = false) Boolean borrowed) {
        return bookService.updateBook(id, name, authorId, imagePath, borrowed);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteBookById(@PathVariable(name = "id") Long id) {
        return bookService.deleteBook(id);
    }

    @PutMapping("/borrowed/{id}")
    public ResponseEntity<Book> borrowedChange(@PathVariable(name = "id") Long id, @RequestParam(required = false) Boolean borrowed) {
        return bookService.borrowedChange(id, borrowed);
    }

}
