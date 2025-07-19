package com.oliveira.gabriel.BookLoanSystem.Controller;

import com.oliveira.gabriel.BookLoanSystem.Dtos.LoanRequest;
import com.oliveira.gabriel.BookLoanSystem.Dtos.LoanResponse;
import com.oliveira.gabriel.BookLoanSystem.Repository.LoanRepository;
import com.oliveira.gabriel.BookLoanSystem.Service.LoanService;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<LoanResponse> makeLoan(@RequestBody LoanRequest ids, JwtAuthenticationToken token){
        return service.makeLoan(ids, token);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LoanResponse> returnLoan(@PathVariable UUID id, JwtAuthenticationToken token){
        return service.returnLoan(id, token);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_admin')")
    public ResponseEntity<Page<LoanResponse>> findAll(Pageable pageable){
        return service.findAll(pageable);
    }

    @GetMapping("/myLoans")
    public ResponseEntity<List<LoanResponse>> viewMyLoans(JwtAuthenticationToken token){
        return service.viewMyLoans(token);
    }

}
