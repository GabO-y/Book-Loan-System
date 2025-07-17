package com.oliveira.gabriel.BookLoanSystem.Repository;

import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
}
