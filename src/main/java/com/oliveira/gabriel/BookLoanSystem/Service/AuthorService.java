package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.AuthorDTO;
import com.oliveira.gabriel.BookLoanSystem.Erros.ContentNotFoundException;
import com.oliveira.gabriel.BookLoanSystem.Erros.BookNotFoundException;
import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Repository.AuthorRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.auditing.AuditingHandler;
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
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository repository, AuthorRepository authorRepository, BookRepository bookRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public ResponseEntity<AuthorDTO> findById(UUID id){

        Optional<Author> author = repository.findById(id);

        if(author.isEmpty()){
            throw new ContentNotFoundException("Author with id: " + id + "could not be found");
        }

        return ResponseEntity.ok(new AuthorDTO(author.get()));

    }

    public ResponseEntity<Page<AuthorDTO>> findAll(Pageable pageable){
        return ResponseEntity.ok(repository
            .findAll(pageable)
            .map(AuthorDTO::new)
        );
    }

    public ResponseEntity<AuthorDTO> insert(AuthorDTO dto){
        return ResponseEntity.ok(new AuthorDTO(repository.save(dtoToEntity(dto))));
    }

    public ResponseEntity<AuthorDTO> edit(AuthorDTO dto){

        Optional<Author> opt = repository.findById(dto.getId());

        if(opt.isEmpty()) throw new ContentNotFoundException("Author with id: " + dto.getId() + " could not be found");

        Author entity = opt.get();

        if(dto.getName() != null) entity.setName(dto.getName());
        if(dto.getDescription() != null) entity.setDescription(dto.getDescription());

        for(var idDto : dto.getBooksId()){

            boolean next = false;

            for(var book : entity.getBooks()){

                if(book.getId() == idDto){
                    next = true;
                    break;
                }
            }

            if(next) continue;

            Optional<Book> book = bookRepository.findById(idDto);

            if(book.isEmpty()) throw new BookNotFoundException(idDto);

            entity.getBooks().add(book.get());

        }

        return ResponseEntity.ok(new AuthorDTO(entity));
    }

    public ResponseEntity<AuthorDTO> delete(UUID id){

        Optional<Author> opt = repository.findById(id);

        if(opt.isEmpty()) throw new ContentNotFoundException("Author with id:" + id + " could not be found");

        AuthorDTO dto = new AuthorDTO(opt.get());

        repository.deleteById(id);

        return ResponseEntity.ok(dto);
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
