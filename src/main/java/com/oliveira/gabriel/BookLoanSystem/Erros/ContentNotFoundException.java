package com.oliveira.gabriel.BookLoanSystem.Erros;

public class ContentNotFoundException extends RuntimeException {
    public ContentNotFoundException(String message) {
        super(message);
    }
}
