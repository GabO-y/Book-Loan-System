package com.oliveira.gabriel.BookLoanSystem.Service;

import com.oliveira.gabriel.BookLoanSystem.Dtos.UserDTO;
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
    public ResponseEntity<Void> createUser(UserDTO dto){

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

    public ResponseEntity<Page<UserDTO>> listUsers(Pageable pageable){
        return ResponseEntity.ok(userRepository.findAll(pageable)
            .map(UserDTO::new));
    }

}
