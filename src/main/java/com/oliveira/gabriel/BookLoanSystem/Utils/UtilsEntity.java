package com.oliveira.gabriel.BookLoanSystem.Utils;

import com.oliveira.gabriel.BookLoanSystem.Erros.BookNotFoundException;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Models.Category;
import com.oliveira.gabriel.BookLoanSystem.Models.User;
import com.oliveira.gabriel.BookLoanSystem.Repository.BookRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.UserRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UtilsEntity {

    UserRepository repository;

    public UtilsEntity(UserRepository repository) {
        this.repository = repository;
    }

    public static List<Book> getBooksDTO(List<UUID> ids){

        if(ids == null || ids.isEmpty()){
            return new ArrayList<>();
        }

        List<Book> books = new ArrayList<>();

        for(UUID id : ids){
            Book book = new Book();
            book.setId(id);
            books.add(book);
        }

        return books;

    }

}
