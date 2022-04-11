package com.vkobinski.bookshelf.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "author")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Author {

    @Id
    @SequenceGenerator(
            name = "author_sequence",
            sequenceName = "author_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "author_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDateTime dateOfBirth;
    @Column(nullable = false)
    private String countryOfBirth;
    @OneToMany
    @JoinColumn(name = "ID_AUTHOR")
    private List<Book> books;

    public Author(String name, LocalDateTime dob, String countryOfBirth, List<Book> books) {
        this.name = name;
        this.dateOfBirth = dob;
        this.countryOfBirth = countryOfBirth;
        this.books = books;
    }
}
