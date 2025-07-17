package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.BookDTO;
import com.oliveira.gabriel.BookLoanSystem.Erros.ContentNotFoundException;
import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Repository.AuthorRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public ResponseEntity<BookDTO> insert(BookDTO dto){
        return ResponseEntity.ok(new BookDTO(bookRepository.save(dtoToEntity(dto))));
    }

    public ResponseEntity<BookDTO> findById(UUID id){

        Optional<Book> book = bookRepository.findById(id);

        return book
            .map(b -> ResponseEntity.ok(new BookDTO(b)))
            .orElse(ResponseEntity.badRequest().build());
    }

    public ResponseEntity<BookDTO> edit(BookDTO dto){

        Optional<Book> opt = bookRepository.findById(dto.getId());

        if(opt.isEmpty()) throw new ContentNotFoundException("Book with id: " + dto.getId() + " not found");

        Book book = opt.get();

        if(dto.getTitle() != null) book.setTitle(dto.getTitle());
        if(dto.getDescription() != null) book.setDescription(dto.getDescription());
        if(dto.getAuthorId() != null) {

            for(var idDto : dto.getAuthorId()){

                boolean next = false;

                for(var author : book.getAuthor()){
                    if(author.getId() == idDto){
                        next = true;
                        break;
                    }
                }

                if(next) continue;

                Optional<Author> o = authorRepository.findById(idDto);

                if(o.isEmpty()) throw new ContentNotFoundException("Author with id: " +  idDto + " not found");

                book.getAuthor().add(o.get());

            }

        }
        if(dto.getCategory() != null) book.setCategory(dto.getCategory());
        if(dto.getPublisher() != null) book.setPublisher(dto.getPublisher());
        if(dto.getAvailable() != null) book.setAvailable(dto.getAvailable());
        if(dto.getOwner() != null) book.setOwner(dto.getOwner());

        bookRepository.save(book);

        return ResponseEntity.ok(new BookDTO(book));

    }

    public ResponseEntity<Page<BookDTO>> getAll(Pageable pageable){
        return ResponseEntity.ok(bookRepository
            .findAll(pageable)
            .map(BookDTO::new)
        );
    }

    public ResponseEntity<BookDTO> delete(UUID id){

        Optional<Book> book = bookRepository.findById(id);

        if(book.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        bookRepository.deleteById(id);

        return ResponseEntity.ok(new BookDTO(book.get()));

    }

    public Book dtoToEntity(BookDTO dto){

        Book entity = new Book();

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());


        entity.setPublisher(dto.getPublisher());
        entity.setCategory(dto.getCategory());
        entity.setOwner(dto.getOwner());

        return entity;

    }



}
