package com.oliveira.gabriel.BookLoanSystem.Dtos;

import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Models.User;
import com.oliveira.gabriel.BookLoanSystem.Repository.BookRepository;

import javax.management.Descriptor;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record UserResponse(UUID id, String name, Set<BookResponse> books) {

    public UserResponse(User user){
        this(user.getId(), user.getUsername(), books(user.getBooks()));
    }

    public static Set<BookResponse> books(Set<Book> books){

        Set<BookResponse> set = new HashSet<>();

        for(var b : books){
            set.add(new BookResponse(b));
        }

        return set;

    }

    public record BookResponse(String title, String description, Boolean available){
        public BookResponse(Book b){
            this(b.getTitle(), b.getDescription(), b.getAvailable());
        }
    }

}
