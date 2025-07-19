package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.BookDTO;
import com.oliveira.gabriel.BookLoanSystem.Dtos.OwnerResponse;
import com.oliveira.gabriel.BookLoanSystem.Erros.ContentNotFoundException;
import com.oliveira.gabriel.BookLoanSystem.Models.*;
import com.oliveira.gabriel.BookLoanSystem.Repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.print.attribute.standard.JobKOctets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final UserRepository userRepository;
    private final LoanItemRepository loanItemRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository, PublisherRepository publisherRepository, UserRepository userRepository, LoanItemRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.userRepository = userRepository;
        this.loanItemRepository = loanRepository;
    }

    public ResponseEntity<BookDTO> insert(BookDTO dto, JwtAuthenticationToken token){

        var user = userRepository.findById(UUID.fromString(token.getName()));

        dto.setOwner(new OwnerResponse(user.get()));

        return ResponseEntity.ok(new BookDTO(bookRepository.save(dtoToEntity(dto, token))));
    }

    public ResponseEntity<BookDTO> findById(UUID id){

        Optional<Book> book = bookRepository.findById(id);

        return book
            .map(b -> ResponseEntity.ok(new BookDTO(b)))
            .orElse(ResponseEntity.badRequest().build());

    }

    public ResponseEntity<BookDTO> edit(BookDTO dto, JwtAuthenticationToken token){

        Optional<Book> opt = bookRepository.findById(dto.getId());

        if(opt.isEmpty()) throw new ContentNotFoundException("Book with id: " + dto.getId() + " not found");

        Book book = opt.get();

        User user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow();

        if(!book.getOwner().equals(user)) {
            if(!userRepository.findByUsername("admin").get().equals(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        if(dto.getTitle() != null) book.setTitle(dto.getTitle());
        if(dto.getDescription() != null) book.setDescription(dto.getDescription());

        if(dto.getAuthorId() != null) {

            for(var idDto : dto.getAuthorId()){

                boolean next = false;

                for(var ety : book.getAuthor()){
                    if(ety.getId() == idDto){
                        next = true;
                        break;
                    }
                }

                if(next) continue;

                Optional<Author> o = authorRepository.findById(idDto);

                if(o.isEmpty()) throw new ContentNotFoundException("Author with id: " +  idDto + " not found");

                book.getAuthor().add(o.get());

            }


            if(dto.getAuthorId().isEmpty()){
                book.setAuthor(new ArrayList<>());
            }

        }

        if(dto.getCategoryId() != null) {

            for(var idDto : dto.getCategoryId()){

                boolean next = false;

                for(var ety : book.getCategory()){
                    if(ety.getId() == idDto){
                        next = true;
                        break;
                    }
                }

                if(next) continue;

                Optional<Category> o = categoryRepository.findById(idDto);

                if(o.isEmpty()) throw new ContentNotFoundException("Category with id: " +  idDto + " not found");

                book.getCategory().add(o.get());

            }

            if(dto.getCategoryId().isEmpty()){
                book.setCategory(new ArrayList<>());
            }

        }

        if(dto.getPublisherId() != null) {

            for(var idDto : dto.getPublisherId()){

                boolean next = false;

                for(var ety : book.getPublisher()){
                    if(ety.getId() == idDto){
                        next = true;
                        break;
                    }
                }

                if(next) continue;

                Optional<Publisher> o = publisherRepository.findById(idDto);

                if(o.isEmpty()) throw new ContentNotFoundException("Publisher with id: " +  idDto + " not found");

                book.getPublisher().add(o.get());

            }


            if(dto.getPublisherId().isEmpty()){
                book.setPublisher(new ArrayList<>());
            }

        }

        if(dto.getAvailable() != null) book.setAvailable(dto.getAvailable());
        if(dto.getOwner() != null) book.setOwner(userRepository.findById(UUID.fromString(token.getName())).get());

        bookRepository.save(book);

        return ResponseEntity.ok(new BookDTO(book));

    }

    public ResponseEntity<Page<BookDTO>> getAll(Pageable pageable){
        return ResponseEntity.ok(bookRepository
            .findAll(pageable)
            .map(BookDTO::new)
        );
    }

    public ResponseEntity<Void> delete(UUID id, JwtAuthenticationToken token) {

        Book book = bookRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book with id: " + id + " could not be found")
        );

        if (!book.getAvailable()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "this book is in a loan, please, end the loan first");
        }

        var user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        );

        UUID ownerId = book.getOwner().getId();
        UUID userId = user.getId();
        UUID adminId = userRepository.findByUsername("admin").orElseThrow().getId();

        if (!userId.equals(ownerId) && !userId.equals(adminId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "this book does not belong to you");
        }

        List<LoanItem> loanItems = loanItemRepository.findAllByBook(book).orElseThrow();
        loanItemRepository.deleteAll(loanItems);

        bookRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }

    private Book dtoToEntity(BookDTO dto, JwtAuthenticationToken token){

        Book entity = new Book();

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());

        List<UUID> a = new ArrayList<>();
        List<UUID> c = new ArrayList<>();
        List<UUID> p = new ArrayList<>();

        if(dto.getAuthorId() != null){
            a = dto.getAuthorId();
        }

        if(dto.getCategoryId() != null){
            c = dto.getCategoryId();
        }

        if(dto.getPublisherId() != null){
            p = dto.getPublisherId();
        }


        Optional<?> opt;

        List<Object> objs = new ArrayList<>();

        if(!a.isEmpty()){

            entity.setAuthor(new ArrayList<>());

            for(var id : a){

                opt = authorRepository.findById(id);

                if(opt.isEmpty()) throw new ContentNotFoundException("Author with id: " + id + " could not be found");

                objs.add(opt.get());

            }

        }

        for(var obj : objs){
            entity.getAuthor().add((Author) obj);
        }

        objs.clear();

        if(!c.isEmpty()){

            entity.setCategory(new ArrayList<>());

            for(var id : c){

                opt = categoryRepository.findById(id);

                if(opt.isEmpty()) throw new ContentNotFoundException("Category with id: " + id + " could not be found");

                objs.add(opt.get());

            }

        }

        for(var obj : objs){
            entity.getCategory().add((Category) obj);
        }

        objs.clear();

        if(!p.isEmpty()){

            entity.setPublisher(new ArrayList<>());

            for(var id : p){

                opt = publisherRepository.findById(id);

                if(opt.isEmpty()) throw new ContentNotFoundException("Publisher with id: " + id + " could not be found");

                objs.add(opt.get());

            }

        }

        for(var obj : objs){
            entity.getPublisher().add((Publisher) obj);
        }


        entity.setOwner(userRepository.findById(UUID.fromString(token.getName())).get());

        return entity;

    }

}
