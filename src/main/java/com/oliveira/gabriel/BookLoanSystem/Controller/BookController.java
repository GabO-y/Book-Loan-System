package com.oliveira.gabriel.BookLoanSystem.Controller;

import com.oliveira.gabriel.BookLoanSystem.Dtos.BookDTO;
import com.oliveira.gabriel.BookLoanSystem.Service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<BookDTO> insert(@Valid @RequestBody BookDTO dto){
        return service.insert(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findById(@PathVariable UUID id){
        return service.findById(id);
    }

    @GetMapping
    public ResponseEntity<Page<BookDTO>> findAll(Pageable pageable){
        return service.getAll(pageable);
    }

    @PatchMapping
    public ResponseEntity<BookDTO> edit(@RequestBody BookDTO dto){
        return service.edit(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDTO> delete(@PathVariable UUID id){
        return service.delete(id);
    }

}
