package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.AuthorDTO;
import com.oliveira.gabriel.BookLoanSystem.Dtos.CategoryDTO;
import com.oliveira.gabriel.BookLoanSystem.Erros.BookNotFoundException;
import com.oliveira.gabriel.BookLoanSystem.Erros.ContentNotFoundException;
import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Models.Category;
import com.oliveira.gabriel.BookLoanSystem.Models.Publisher;
import com.oliveira.gabriel.BookLoanSystem.Repository.BookRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.CategoryRepository;
import com.oliveira.gabriel.BookLoanSystem.Utils.UtilsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.smartcardio.TerminalFactorySpi;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final BookRepository bookRepository;

    public CategoryService(CategoryRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    public ResponseEntity<CategoryDTO> insert(CategoryDTO dto){
        return ResponseEntity.ok(new CategoryDTO(repository.save(dtoToEntity(dto))));
    }

    public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable){
        return ResponseEntity.ok(repository
            .findAll(pageable)
            .map(CategoryDTO::new)
        );
    }

    public ResponseEntity<CategoryDTO> findById(UUID id){

        Optional<Category> opt = repository.findById(id);

        if(opt.isEmpty()) throw new ContentNotFoundException("Category with id: " + id + " could not be found");

        return ResponseEntity.ok(new CategoryDTO(opt.get()));

    }

    public ResponseEntity<CategoryDTO> edit(CategoryDTO dto){

        Optional<Category> opt = repository.findById(dto.getId());

        if(opt.isEmpty()) throw new ContentNotFoundException("Category with id: " + dto.getId() + " could not be found");

        Category entity = opt.get();

        if(dto.getName() != null) entity.setName(dto.getName());

        if(dto.getDescription() != null) entity.setDescription(dto.getDescription());

        if(dto.getBooksId() != null){

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

        }



        return ResponseEntity.ok(new CategoryDTO(entity));
    }


    public ResponseEntity<CategoryDTO> delete(UUID id){

        Optional<Category> opt = repository.findById(id);

        if(opt.isEmpty()) throw new ContentNotFoundException("Category with id:" + id + " could not be found");

        CategoryDTO dto = new CategoryDTO(opt.get());

        repository.deleteById(id);

        return ResponseEntity.ok(dto);
    }

    private Category dtoToEntity(CategoryDTO dto){

        Category entity = new Category();

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setBooks(UtilsEntity.getBooksDTO(dto.getBooksId()));

        return entity;

    }


}
