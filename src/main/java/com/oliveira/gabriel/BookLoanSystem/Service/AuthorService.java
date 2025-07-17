package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.AuthorDTO;
import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Repository.AuthorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    private final AuthorRepository repository;

    private final BookService bookService;


    public AuthorService(AuthorRepository repository, BookService bookService) {
        this.repository = repository;
        this.bookService = bookService;
    }

    public ResponseEntity<AuthorDTO> findById(UUID id){

        Optional<Author> author = repository.findById(id);

        if(author.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new AuthorDTO(author.get()));

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

            for(var i : dto.getBooksId()){

                Book b = new Book();

                b.setId(i);

                entity.getBooks().add(b);

            }

        }

        return entity;

    }

}
