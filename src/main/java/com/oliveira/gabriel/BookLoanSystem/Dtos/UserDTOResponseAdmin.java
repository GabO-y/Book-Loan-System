package com.oliveira.gabriel.BookLoanSystem.Dtos;

import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Models.Loan;
import com.oliveira.gabriel.BookLoanSystem.Models.LoanItem;
import com.oliveira.gabriel.BookLoanSystem.Models.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserDTOResponseAdmin(UUID id, String username, String password, Set<BookAdminResponse> books, Set<LoanAdminResponse> loans) {

    public UserDTOResponseAdmin(User u){
        this(u.getId(), u.getUsername(), u.getPassword(), books(u.getBooks()), loans(u.getLoans()));
    }

    public static Set<BookAdminResponse> books(Set<Book> books){
        Set<BookAdminResponse> set = new HashSet<>();
        for(var b: books){
            set.add(new BookAdminResponse(b));
        }
        return set;
    }

    public static Set<LoanAdminResponse> loans(Set<Loan> loans){
        Set<LoanAdminResponse> set = new HashSet<>();
        for(var l: loans){
            set.add(new LoanAdminResponse(l));
        }
        return set;
    }



    public record BookAdminResponse(String title, String description, Boolean available){

        public BookAdminResponse(Book b){
            this(b.getTitle(), b.getDescription(), b.getAvailable());
        }

    }

    public record LoanAdminResponse(UUID id, Set<BookAdminResponse> books, Boolean finalized){
        public LoanAdminResponse(Loan loan){
            this(loan.getId(), UserDTOResponseAdmin.books(loan.getItem().stream().map(LoanItem::getBook).collect(Collectors.toSet())), loan.getFinalized());
        }
    }

}
