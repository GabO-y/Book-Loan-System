package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.AuthorDTO;
import com.oliveira.gabriel.BookLoanSystem.Erros.ContentNotFound;
import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
            throw new ContentNotFound("Author with id: " + id + "could not be found");
        }

        return ResponseEntity.ok(new AuthorDTO(author.get()));

    }

    //Fazendo o getALl de author
    public ResponseEntity<Page<AuthorDTO>> findAll(Pageable pageable){
        return ResponseEntity.ok(repository
            .findAll(pageable)
            .map(AuthorDTO::new)
        );
    }

    public ResponseEntity<AuthorDTO> insert(AuthorDTO dto){
        return ResponseEntity.ok(new AuthorDTO(repository.save(dtoToEntity(dto))));
    }



    public Author dtoToEntity(AuthorDTO dto){

        Author entity = new Author();

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if(dto.getBooksId() == null || dto.getBooksId().isEmpty()) {
            entity.setBooks(List.of());
        }else{

            entity.setBooks(new ArrayList<>());

            for(var i : dto.getBooksId()){

                Book b = new Book();

                b.setId(i);

                entity.getBooks().add(b);

            }

        }

        return entity;

    }

}
