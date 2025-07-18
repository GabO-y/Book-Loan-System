package com.oliveira.gabriel.BookLoanSystem.Repository;

import com.oliveira.gabriel.BookLoanSystem.Models.Role;
import com.oliveira.gabriel.BookLoanSystem.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String name);
}
