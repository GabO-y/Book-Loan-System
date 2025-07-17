package com.oliveira.gabriel.BookLoanSystem.Repository;

import com.oliveira.gabriel.BookLoanSystem.Dtos.BookDTO;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Page<Book> findAll(Pageable pageable);
}
