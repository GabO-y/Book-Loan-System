package com.oliveira.gabriel.BookLoanSystem.Repository;

import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Models.LoanItem;
import com.oliveira.gabriel.BookLoanSystem.Models.LoanItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanItemRepository extends JpaRepository<LoanItem, LoanItemId> {

    Optional<List<LoanItem>> findAllByBook(Book book);

}
