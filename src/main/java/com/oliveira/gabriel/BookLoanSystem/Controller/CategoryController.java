package com.oliveira.gabriel.BookLoanSystem.Controller;

import com.oliveira.gabriel.BookLoanSystem.Dtos.AuthorDTO;
import com.oliveira.gabriel.BookLoanSystem.Dtos.CategoryDTO;
import com.oliveira.gabriel.BookLoanSystem.Models.Category;
import com.oliveira.gabriel.BookLoanSystem.Service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
        return service.insert(dto);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable){
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable UUID id){
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> delete(@PathVariable UUID id){
        return service.delete(id);
    }

}
