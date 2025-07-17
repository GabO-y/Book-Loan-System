package com.oliveira.gabriel.BookLoanSystem.Utils;

import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Repository.BookRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UtilsEntity {

    public static List<Book> getBooksDTO(List<UUID> ids){

        if(ids == null || ids.isEmpty()){
            return new ArrayList<>(List.of());
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
