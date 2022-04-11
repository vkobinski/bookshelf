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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public ResponseEntity<List<Author>> getAuthors() {
        return ResponseEntity.ok(authorRepository.findAll());
    }

    public ResponseEntity<Author> createAuthor(Author author) {
        try {
            return ResponseEntity.ok(authorRepository.save(author));
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Author> getAuthorById(Long id) {
        try {
            return authorRepository.findById(id)
                    .map((author) -> ResponseEntity.ok().body(author))
                    .orElseThrow(() -> {
                        throw new IllegalStateException("Not found Author with ID: " + id);
                    });
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<Author> updateAuthorById(Long id, String name, String countryOfBirth, LocalDateTime    dateOfBirth) {
        try {
            Author existingAuthor = authorRepository.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Could not find author with ID: " + id));
            if(name != null && name.length() > 0 && !name.equals(existingAuthor.getName())){
                existingAuthor.setName(name);
            }
            if(countryOfBirth != null && countryOfBirth.length() > 0 && !countryOfBirth.equals(existingAuthor.getCountryOfBirth())){
                existingAuthor.setCountryOfBirth(countryOfBirth);
            }
            if(dateOfBirth != null && !dateOfBirth.equals(existingAuthor.getDateOfBirth())){
                existingAuthor.setCountryOfBirth(countryOfBirth);
            }
            return ResponseEntity.ok(existingAuthor);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.badRequest().build();
        }
    }
    public ResponseEntity<Object> deleteById(Long id) {
        log.info("Deleting author with ID: " + id);
        try {
            return authorRepository.findById(id)
                    .map((author) -> {
                        author.getBooks().forEach((book) -> {
                            var foundBook = bookRepository.findById(book.getId());
                            if(foundBook.isPresent()){
                                log.info("Deleting book with ID: " + book.getId() + ", Name: " + book.getName());
                                bookRepository.deleteById(book.getId());
                            }
                        });
                        authorRepository.deleteById(id);
                        return ResponseEntity.noContent().build();
                    })
                    .orElseThrow(() -> new IllegalStateException("Author with ID " + id + " not found"));
        }catch (Exception e) {
            log.error(e.toString());
            log.info("Error when deleting author with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
