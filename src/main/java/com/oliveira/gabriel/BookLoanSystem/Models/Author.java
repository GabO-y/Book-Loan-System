package com.oliveira.gabriel.BookLoanSystem.Models;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ManyToAny;

import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    @ManyToMany(mappedBy = "author")
    private List<Book> books;

    public Author(){
        books = List.of();
    }

}
