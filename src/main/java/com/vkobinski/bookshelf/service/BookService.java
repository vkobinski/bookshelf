package com.vkobinski.bookshelf.service;

import com.vkobinski.bookshelf.model.Author;
import com.vkobinski.bookshelf.model.Book;
import com.vkobinski.bookshelf.repository.AuthorRepository;
import com.vkobinski.bookshelf.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    public ResponseEntity<Book> getBookById(Long id) {
        try {
            return bookRepository.findById(id)
                    .map((book) -> ResponseEntity.ok().body(book))
                    .orElseThrow(() -> {
                        throw new IllegalStateException("Not found Book with ID: " + id);
                    });
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Book> createBook(Book book) {
        try{
            Optional<Author> author = authorRepository.findById(book.getAuthor().getId());
            if(author.isPresent()){
                book.setAuthor(author.get());
                return ResponseEntity.ok(bookRepository.save(book));
            }
            else {
                throw new IllegalStateException("Could not find Author with ID: " + book.getAuthor().getId());
            }
        }catch(Exception e){
            log.error(e.toString());
            return ResponseEntity.badRequest().build();
        }

    }

    public ResponseEntity<Object> deleteBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            bookRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        else{
            log.error("Could not find Book with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<Book> updateBook(Long id, String name, Long authorId, String imagePath, Boolean borrowed){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if(name != null && name.length() > 0){
                book.setName(name);
            }
            if(authorId != null && authorId > 0) {
                Optional<Author> authorOptional = authorRepository.findById(authorId);
                authorOptional.ifPresent(book::setAuthor);
            }
            if(imagePath != null && imagePath.length() > 0 && !imagePath.equals(book.getImagePath())){
                book.setImagePath(imagePath);
            }
            if(borrowed != null){
                book.setBorrowed(borrowed);
            }

            return ResponseEntity.ok(bookOptional.get());
        }else{
            log.error("Could not find book with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<Book> borrowedChange(Long id, Boolean borrowed) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()) {
            if (borrowed == null) {
                bookOptional.get().setBorrowed(!bookOptional.get().getBorrowed());
            }else {
                bookOptional.get().setBorrowed(borrowed);
            }
            return ResponseEntity.ok(bookOptional.get());
        }else{
            log.error("Could not find book with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Object> deleteById(Long id) {
        log.info("Deleting book with ID: " + id);
        try {
            return bookRepository.findById(id)
                    .map((author) -> {
                        bookRepository.deleteById(id);
                        return ResponseEntity.noContent().build();
                    })
                    .orElseThrow(() -> new IllegalStateException("Book with ID " + id + " could not found"));
        }catch (Exception e) {
            log.error(e.toString());
            log.info("Error when deleting Book with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
