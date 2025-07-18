package com.oliveira.gabriel.BookLoanSystem.Controller;

import com.oliveira.gabriel.BookLoanSystem.Dtos.BookDTO;
import com.oliveira.gabriel.BookLoanSystem.Service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    public ResponseEntity<BookDTO> insert(@RequestBody BookDTO dto, JwtAuthenticationToken token){
        return service.insert(dto, token);
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
    public ResponseEntity<BookDTO> edit(@RequestBody BookDTO dto, JwtAuthenticationToken token){
        return service.edit(dto, token);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, JwtAuthenticationToken token){
        return service.delete(id, token);
    }

}
