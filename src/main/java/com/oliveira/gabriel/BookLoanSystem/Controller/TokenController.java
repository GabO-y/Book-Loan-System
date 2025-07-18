package com.oliveira.gabriel.BookLoanSystem.Controller;

import com.nimbusds.jwt.JWTClaimsSet;
import com.oliveira.gabriel.BookLoanSystem.Dtos.LoginRequest;
import com.oliveira.gabriel.BookLoanSystem.Dtos.LoginResponse;
import com.oliveira.gabriel.BookLoanSystem.Models.Role;
import com.oliveira.gabriel.BookLoanSystem.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping(("/login"))
public class TokenController {

    private final JwtEncoder encoder;
    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder encoder, UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.encoder = encoder;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> loginRequest(@RequestBody LoginRequest loginRequest){

        var user = repository.findByUsername(loginRequest.username());

        if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)){
            throw new BadCredentialsException("user or password is incorrect");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var scopes = user.get().getRoles()
            .stream()
            .map(Role::getName)
            .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
            .issuer("backend")
            .subject(user.get().getId().toString())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiresIn))
            .claim("scope", scopes)
            .build();

        var jwtValue = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));


    }

}
