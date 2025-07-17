package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.AuthorDTO;
import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Repository.AuthorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<AuthorDTO> findById(UUID id){

        Optional<Author> author = repository.findById(id);

        if(author.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new AuthorDTO(author.get()));

    }

}
