package com.oliveira.gabriel.BookLoanSystem.Dtos;

public record LoginResponse(String accessToken, Long expiredIn) {
}
