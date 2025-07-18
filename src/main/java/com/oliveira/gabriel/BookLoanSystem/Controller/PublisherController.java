package com.oliveira.gabriel.BookLoanSystem.Controller;


import com.oliveira.gabriel.BookLoanSystem.Dtos.CategoryDTO;
import com.oliveira.gabriel.BookLoanSystem.Dtos.PublisherDTO;
import com.oliveira.gabriel.BookLoanSystem.Models.Publisher;
import com.oliveira.gabriel.BookLoanSystem.Service.PublisherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService service;

    public PublisherController(PublisherService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PublisherDTO> insert(@RequestBody PublisherDTO dto){
        return service.insert(dto);
    }

    @GetMapping
    public ResponseEntity<Page<PublisherDTO>> findAll(Pageable pageable){
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> findById(@PathVariable UUID id){
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PublisherDTO> delete(@PathVariable UUID id){
        return service.delete(id);
    }

}
