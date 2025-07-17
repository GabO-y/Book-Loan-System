package com.oliveira.gabriel.BookLoanSystem.Dtos;

import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<AuthorDTO> author;
    private String category;
    private String publisher;
    private String owner;
    private Boolean available;

    public BookDTO(Book book){
        id = book.getId();
        title = book.getTitle();
        description = book.getDescription();
        author = book.getAuthor()
            .stream()
            .map(AuthorDTO::new)
            .toList();
        publisher = book.getPublisher();
        category = book.getCategory();
        owner = book.getOwner();
        available = book.getAvailable();
    }

}
