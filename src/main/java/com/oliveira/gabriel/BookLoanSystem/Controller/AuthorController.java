package com.oliveira.gabriel.BookLoanSystem.Controller;

import com.oliveira.gabriel.BookLoanSystem.Dtos.AuthorDTO;
import com.oliveira.gabriel.BookLoanSystem.Models.Author;
import com.oliveira.gabriel.BookLoanSystem.Service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> insert(@RequestBody AuthorDTO dto){
        return service.insert(dto);
    }

    @GetMapping
    public ResponseEntity<Page<AuthorDTO>> findAll(Pageable pageable){
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> findById(@PathVariable UUID id){
            return service.findById(id);
    }

    @PatchMapping
    public ResponseEntity<AuthorDTO> edit(@RequestBody AuthorDTO dto){
        return service.edit(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorDTO> delete(@PathVariable UUID id){
        return service.delete(id);
    }

}
