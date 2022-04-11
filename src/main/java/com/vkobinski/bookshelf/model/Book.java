package com.vkobinski.bookshelf.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@ToString
//@JsonIdentityInfo(
        //generator = ObjectIdGenerators.PropertyGenerator.class,
        //property = "id")
public class Book {

    @Id
    @SequenceGenerator(
            name = "book_sequence",
            sequenceName = "book_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "book_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name="ID_AUTHOR", nullable=false)
    private Author author;
    @Column(nullable = false)
    private String imagePath;
    @Column(nullable = false)
    private Boolean borrowed;



    public Book(String name, Author author, String imagePath, Boolean borrowed) {
        this.name = name;
        this.author = author;
        this.imagePath = imagePath;
        this.borrowed = borrowed;
    }
}
