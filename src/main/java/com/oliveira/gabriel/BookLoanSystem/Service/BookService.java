package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.BookDTO;
import com.oliveira.gabriel.BookLoanSystem.Erros.ContentNotFoundException;
import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Models.Category;
import com.oliveira.gabriel.BookLoanSystem.Models.Publisher;
import com.oliveira.gabriel.BookLoanSystem.Repository.AuthorRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.BookRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.CategoryRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.PublisherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
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

        }

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

        List<UUID> a = dto.getAuthorId();
        List<UUID> c = dto.getCategoryId();
        List<UUID> p = dto.getPublisherId();

        Optional<?> opt;

        List<Object> objs = new ArrayList<>();

        if(a != null && !a.isEmpty()){

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

        if(c != null && !c.isEmpty()){

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

        if(p != null && !p.isEmpty()){

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


        entity.setOwner(dto.getOwner());

        return entity;

    }



}
