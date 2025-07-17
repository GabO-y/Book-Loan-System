package com.oliveira.gabriel.BookLoanSystem.Repository;

import com.oliveira.gabriel.BookLoanSystem.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
