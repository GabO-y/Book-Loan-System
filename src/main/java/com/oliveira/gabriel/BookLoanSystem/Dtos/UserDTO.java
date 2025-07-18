package com.oliveira.gabriel.BookLoanSystem.Dtos;

import com.oliveira.gabriel.BookLoanSystem.Models.User;

public record UserDTO(String username, String password) {

    public UserDTO(User u){
        this(u.getUsername(), u.getPassword());
    }

}
