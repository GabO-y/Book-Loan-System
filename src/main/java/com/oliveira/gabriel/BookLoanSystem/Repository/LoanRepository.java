package com.oliveira.gabriel.BookLoanSystem.Repository;


import com.oliveira.gabriel.BookLoanSystem.Models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {
}
