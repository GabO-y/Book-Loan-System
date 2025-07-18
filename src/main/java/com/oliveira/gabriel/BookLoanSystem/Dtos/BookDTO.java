package com.oliveira.gabriel.BookLoanSystem.Dtos;

import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Models.Category;
import com.oliveira.gabriel.BookLoanSystem.Models.Publisher;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private UUID id;
    @NotNull
    private String title;
    @NotNull
    private String description;
    private List<UUID> authorId;
    private List<UUID> categoryId;
    private List<UUID> publisherId;
    private OwnerResponse owner;
    private Boolean available;

    public BookDTO(Book book){
        id = book.getId();
        title = book.getTitle();
        description = book.getDescription();

        authorId = new ArrayList<>();
        categoryId = new ArrayList<>();
        publisherId = new ArrayList<>();

        if(book.getAuthor() == null){
            book.setAuthor(new ArrayList<>());
        }

        if(book.getPublisher() == null){
            book.setPublisher(new ArrayList<>());
        }

        if(book.getCategory() == null){
            book.setCategory(new ArrayList<>());
        }

        for(var a : book.getAuthor()){
            authorId.add(a.getId());
        }

        for(var p : book.getPublisher()){
            publisherId.add(p.getId());
        }

        for(var c : book.getCategory()){
            categoryId.add(c.getId());
        }


        owner = new OwnerResponse(book.getOwner());
        available = book.getAvailable();
    }

}
