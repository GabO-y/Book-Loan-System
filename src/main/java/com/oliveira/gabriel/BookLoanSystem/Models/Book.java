package com.oliveira.gabriel.BookLoanSystem.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;
    @ManyToMany(mappedBy = "books")
    private List<Author> author;
    private String category;
    private String publisher;
    private String owner;
    private Boolean available;
    private LocalDateTime timePost;

    public Book(){
        available = true;
        timePost = LocalDateTime.now();
    }

}
