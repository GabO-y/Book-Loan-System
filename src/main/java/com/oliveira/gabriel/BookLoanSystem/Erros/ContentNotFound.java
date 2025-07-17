package com.oliveira.gabriel.BookLoanSystem.Erros;

public class ContentNotFound extends RuntimeException {
    public ContentNotFound(String message) {
        super(message);
    }
}
