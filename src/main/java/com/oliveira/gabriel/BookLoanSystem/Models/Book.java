package com.oliveira.gabriel.BookLoanSystem.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ManyToAny;

import java.awt.event.ItemListener;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;
    @ManyToMany()
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> author;
    @ManyToMany()
    @JoinTable(
        name = "book_category",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> category;
    @ManyToMany()
    @JoinTable(
        name = "book_publisher",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "publisher_id")
    )
    private List<Publisher> publisher;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    private Boolean available;
    private LocalDateTime timePost;

    @OneToOne
    @JsonIgnore
    private Loan loanJoin;

    public Book(){
        available = true;
        timePost = LocalDateTime.now();
    }

}
