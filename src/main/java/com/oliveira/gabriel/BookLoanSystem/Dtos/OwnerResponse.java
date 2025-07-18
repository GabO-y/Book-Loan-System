package com.oliveira.gabriel.BookLoanSystem.Dtos;

import com.oliveira.gabriel.BookLoanSystem.Models.User;

import java.util.UUID;

public record OwnerResponse (String username, UUID id){

    public OwnerResponse(User user){
        this(user.getUsername(), user.getId());
    }

}
