package com.oliveira.gabriel.BookLoanSystem.Erros;

import java.util.UUID;

public class BookNotFoundException extends ContentNotFoundException {
    public BookNotFoundException(UUID id) {
        super("Book with id: " + id + " could not be found");
    }
}
