package com.oliveira.gabriel.BookLoanSystem.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_loan_items")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoanItem {

    @EmbeddedId
    private LoanItemId id = new LoanItemId();

    @JsonIgnore
    @ManyToOne
    @MapsId("loanId")
    private Loan loan;


    @JsonIgnore
    @ManyToOne
    @MapsId("bookId")
    private Book book;

    public LoanItem(Loan loan, Book book) {
        this.loan = loan;
        this.book = book;
        this.id = new LoanItemId(loan.getId(), book.getId());
    }



}
