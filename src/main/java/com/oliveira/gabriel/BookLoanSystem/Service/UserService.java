package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.UserDTOResponseAdmin;
import com.oliveira.gabriel.BookLoanSystem.Dtos.UserResponse;
import com.oliveira.gabriel.BookLoanSystem.Models.Role;
import com.oliveira.gabriel.BookLoanSystem.Models.User;
import com.oliveira.gabriel.BookLoanSystem.Repository.RoleRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Transactional
    public ResponseEntity<Void> createUser(UserDTOResponseAdmin dto){

        var user = userRepository.findByUsername(dto.username());

        if(user.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User u = new User();

        u.setUsername(dto.username());
        u.setPassword(encoder.encode(dto.password()));
        u.setRoles(Set.of(roleRepository.findByName(Role.Values.BASIC.name())));

        userRepository.save(u);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<UserResponse> findById(UUID id){
        return ResponseEntity.ok(new UserResponse(userRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        )));
    }

    public ResponseEntity<Page<UserDTOResponseAdmin>> listUsers(Pageable pageable){
        return ResponseEntity.ok(userRepository.findAll(pageable)
            .map(UserDTOResponseAdmin::new));
    }

}
