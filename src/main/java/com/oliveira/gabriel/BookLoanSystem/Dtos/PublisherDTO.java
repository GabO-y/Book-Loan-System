package com.oliveira.gabriel.BookLoanSystem.Dtos;

import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Models.Publisher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherDTO {

    private UUID id;
    private String name;
    private String description;
    private List<UUID> booksId;

    public PublisherDTO(Publisher a){
        id = a.getId();
        name = a.getName();
        description = a.getDescription();
        booksId = a.getBooks()
            .stream()
            .map(Book::getId)
            .toList();
    }

}
