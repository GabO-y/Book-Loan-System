package com.oliveira.gabriel.BookLoanSystem.Models;


import com.nimbusds.jwt.JWTClaimsSet;
import com.oliveira.gabriel.BookLoanSystem.Dtos.LoginRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;
    private String password;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch =  FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "owner")
    private Set<Book> books;

    public boolean isLoginCorrect(LoginRequest request, PasswordEncoder encoder){
        return encoder.matches(request.password(), this.password);
    }


}
