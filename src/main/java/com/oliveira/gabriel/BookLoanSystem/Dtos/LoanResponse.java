package com.oliveira.gabriel.BookLoanSystem.Dtos;

import com.oliveira.gabriel.BookLoanSystem.Models.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanResponse {

    private UUID id;
    private Set<BookLoanResponse> loans;
    private Instant begin;
    private Instant end;
    private Boolean finalized;
    private OwnerResponse owner;
    private String action;

    public LoanResponse(Loan loan, String action){

        id = loan.getId();
        loans = new HashSet<>();

        for(var item : loan.getItem()){
            loans.add(new BookLoanResponse(item.getBook().getTitle(), item.getBook().getDescription()));
        }

        begin = loan.getCreateAt();
        finalized = loan.getFinalized();
        end = loan.getEndAt();

        this.action = action;

        if(loan.getBorrower() == null){
            owner = null;
        }else{
            owner = new OwnerResponse(loan.getBorrower());
        }
    }

    public record BookLoanResponse(String title, String description){}

}
