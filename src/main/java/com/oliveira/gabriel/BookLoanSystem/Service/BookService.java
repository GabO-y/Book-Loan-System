package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.AuthorDTO;
import com.oliveira.gabriel.BookLoanSystem.Dtos.BookDTO;
import com.oliveira.gabriel.BookLoanSystem.Erros.ContentNotFound;
import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Repository.AuthorRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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

        bookRepository.save(dtoToEntityBook(dto));

        return ResponseEntity.ok(dto);

    }

    public ResponseEntity<BookDTO> findById(UUID id){

        Optional<Book> book = bookRepository.findById(id);

        return book
            .map(b -> ResponseEntity.ok(new BookDTO(b)))
            .orElse(ResponseEntity.badRequest().build());
    }

    public ResponseEntity<BookDTO> editAll(BookDTO edit){

        Optional<Book> book = bookRepository.findById(edit.getId());

        if(book.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        book.get().setTitle(edit.getTitle());
        book.get().setDescription(edit.getDescription());

        book.get().getAuthor().clear();

        for(var a : edit.getAuthor()){

            Optional<Author> author = authorRepository.findById(a.getId());

            if(author.isEmpty()){
                throw new ContentNotFound("Author with id: " + a.getId() + " not could be found");
            }

            book.get().getAuthor().add(author.get());

        }

        book.get().setCategory(edit.getCategory());
        book.get().setPublisher(edit.getPublisher());
        book.get().setOwner(edit.getOwner());
        book.get().setAvailable(edit.getAvailable());

        return ResponseEntity.ok(new BookDTO(bookRepository.save(book.get())));

    }

    public ResponseEntity<BookDTO> edit(BookDTO edit){

        Optional<Book> book = bookRepository.findById(edit.getId());

        //Tem usando essa funcão que fica ate mais facil
        //Book b = bookRepository.getReferenceById()

        if(book.isEmpty()){
            return ResponseEntity.noContent().build();
        }


        if(edit.getTitle() != null){
            book.get().setTitle(edit.getTitle());
        }
        if(edit.getDescription() != null){
            book.get().setDescription(edit.getDescription());
        }
        if(edit.getAuthor() != null){

            List<AuthorDTO> authors = edit.getAuthor();

            for(var a : edit.getAuthor()){

                Optional<Author> author = authorRepository.findById(a.getId());

                if(author.isEmpty()){
                    throw new ContentNotFound("Author with id: " + a.getId() + " not could be found");
                }

                boolean next = false;

                for(var aut : book.get().getAuthor()){

                    if(aut.getId() == a.getId()){
                        next = true;
                        break;
                    }

                }

                if(next){
                    continue;
                }

                book.get().getAuthor().add(dtoToEntityAuthor(a));
            }


        }
        if(edit.getPublisher() != null){
            book.get().setPublisher(edit.getPublisher());
        }
        if(edit.getCategory() != null){
            book.get().setCategory(edit.getCategory());
        }
        if(edit.getAvailable() != null){
            book.get().setAvailable(edit.getAvailable());
        }
        if(edit.getOwner() != null){
            book.get().setOwner(edit.getOwner());
        }

        return ResponseEntity.ok(new BookDTO(bookRepository.save(book.get())));

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


    public Book dtoToEntityBook(BookDTO dto){

        Book entity = new Book();

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());

        for(var a : dto.getAuthor()){
            entity.getAuthor().add(dtoToEntityAuthor(a));
        }

        entity.setPublisher(dto.getPublisher());
        entity.setCategory(dto.getCategory());
        entity.setOwner(dto.getOwner());

        return entity;

    }

    public Author dtoToEntityAuthor(AuthorDTO dto){

        Author entity = new Author();

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        for(var i : dto.getBooks()){
            entity.getBooks().add(dtoToEntityBook(i));
        }

        return entity;

    }

}
