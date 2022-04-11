package com.vkobinski.bookshelf.configuration;

import com.vkobinski.bookshelf.model.Author;
import com.vkobinski.bookshelf.model.Book;
import com.vkobinski.bookshelf.repository.AuthorRepository;
import com.vkobinski.bookshelf.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Collections;

@Configuration
public class BookConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(BookRepository bookRepository, AuthorRepository authorRepository) {
        return args -> {
            Author author = new Author("Victor Kobinski", LocalDateTime.now(), "Brazil", Collections.emptyList());
            authorRepository.save(author);
            Book book = new Book("Teste", authorRepository.findById(1l).get(), "./", false);
            bookRepository.save(book);
        };
    }

}
