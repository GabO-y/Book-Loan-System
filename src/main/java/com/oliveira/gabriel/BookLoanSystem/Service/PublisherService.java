package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.AuthorDTO;
import com.oliveira.gabriel.BookLoanSystem.Dtos.CategoryDTO;
import com.oliveira.gabriel.BookLoanSystem.Dtos.PublisherDTO;
import com.oliveira.gabriel.BookLoanSystem.Erros.BookNotFoundException;
import com.oliveira.gabriel.BookLoanSystem.Erros.ContentNotFoundException;
import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Models.Category;
import com.oliveira.gabriel.BookLoanSystem.Models.Publisher;
import com.oliveira.gabriel.BookLoanSystem.Repository.BookRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.PublisherRepository;
import com.oliveira.gabriel.BookLoanSystem.Utils.UtilsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class PublisherService {

    private final PublisherRepository repository;
    private final BookRepository bookRepository;

    public PublisherService(PublisherRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    public ResponseEntity<PublisherDTO> insert(PublisherDTO dto){
        return ResponseEntity.ok(new PublisherDTO(repository.save(dtoToEntity(dto))));
    }

    public ResponseEntity<Page<PublisherDTO>> findAll(Pageable pageable){
        return ResponseEntity.ok(repository
            .findAll(pageable)
            .map(PublisherDTO::new)
        );
    }

    public ResponseEntity<PublisherDTO> findById(UUID id){

        Optional<Publisher> opt = repository.findById(id);

        if(opt.isEmpty()) throw new ContentNotFoundException("Publisher with id: " + id + " could not be found");

        return ResponseEntity.ok(new PublisherDTO(opt.get()));

    }

    public ResponseEntity<PublisherDTO> edit(PublisherDTO dto){

        Optional<Publisher> opt = repository.findById(dto.getId());

        if(opt.isEmpty()) throw new ContentNotFoundException("Publisher with id: " + dto.getId() + " could not be found");

        Publisher entity = opt.get();

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

        if(dto.getBooksId().isEmpty()){
            entity.setBooks(new ArrayList<>());
        }

        return ResponseEntity.ok(new PublisherDTO(entity));
    }

    public ResponseEntity<PublisherDTO> delete(UUID id){

        Optional<Publisher> opt = repository.findById(id);

        if(opt.isEmpty()) throw new ContentNotFoundException("Publisher with id:" + id + " could not be found");

        PublisherDTO dto = new PublisherDTO(opt.get());

        repository.deleteById(id);

        return ResponseEntity.ok(dto);
    }

    private Publisher dtoToEntity(PublisherDTO dto){

        Publisher entity = new Publisher();

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setBooks(UtilsEntity.getBooksDTO(dto.getBooksId()));

        return entity;

    }

}
