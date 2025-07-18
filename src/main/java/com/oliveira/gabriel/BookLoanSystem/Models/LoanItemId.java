package com.oliveira.gabriel.BookLoanSystem.Models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanItemId implements Serializable {
    private UUID loanId;
    private UUID bookId;

}
