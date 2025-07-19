package com.oliveira.gabriel.BookLoanSystem.Controller;

import com.oliveira.gabriel.BookLoanSystem.Dtos.LoginRequest;
import com.oliveira.gabriel.BookLoanSystem.Dtos.UserDTOResponseAdmin;
import com.oliveira.gabriel.BookLoanSystem.Dtos.UserResponse;
import com.oliveira.gabriel.BookLoanSystem.Service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDTOResponseAdmin dto){
        return service.createUser(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id){
        return service.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_admin')")
    public ResponseEntity<Page<UserDTOResponseAdmin>> listUsers(Pageable pageable){
        return service.listUsers(pageable);
    }

    @PatchMapping
    public ResponseEntity<String> edit(LoginRequest login, JwtAuthenticationToken token){
        return service.edit(login, token);
    }

}
