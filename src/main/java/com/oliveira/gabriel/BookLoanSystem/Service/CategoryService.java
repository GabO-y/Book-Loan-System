package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.CategoryDTO;
import com.oliveira.gabriel.BookLoanSystem.Erros.ContentNotFoundException;
import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Models.Book;
import com.oliveira.gabriel.BookLoanSystem.Models.Category;
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

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
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

    private Category dtoToEntity(CategoryDTO dto){

        Category entity = new Category();

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setBooks(UtilsEntity.getBooksDTO(dto.getBooksId()));

        return entity;

    }


}
