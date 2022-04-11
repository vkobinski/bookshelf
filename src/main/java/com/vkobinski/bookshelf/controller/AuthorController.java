package com.vkobinski.bookshelf.controller;

import com.vkobinski.bookshelf.model.Author;
import com.vkobinski.bookshelf.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/author")
public class AuthorController {


    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAuthors() {
        return authorService.getAuthors();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable(name = "id") Long id) {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        return authorService.createAuthor(author);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Author> updateAuthorById(@PathVariable(name = "id") Long id,
                                                   @RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String countryOfBirth,
                                                   @RequestParam(required = false) LocalDateTime dateOfBirth) {
        return authorService.updateAuthorById(id, name, countryOfBirth, dateOfBirth);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteAuthorById(@PathVariable(name = "id") Long id) {
        return authorService.deleteById(id);
    }
}
