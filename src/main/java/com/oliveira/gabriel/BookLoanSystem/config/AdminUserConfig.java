package com.oliveira.gabriel.BookLoanSystem.config;

import com.oliveira.gabriel.BookLoanSystem.Models.Role;
import com.oliveira.gabriel.BookLoanSystem.Models.User;
import com.oliveira.gabriel.BookLoanSystem.Repository.RoleRepository;
import com.oliveira.gabriel.BookLoanSystem.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder encoder;

    public AdminUserConfig(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Role role = roleRepository.findByName(Role.Values.ADMIN.name());
        var admin = userRepository.findByUsername("admin");

        admin.ifPresentOrElse(
            user -> {
                System.out.println("Admin already exist");
            },
            () -> {
                User user = new User();
                user.setUsername("admin");
                user.setPassword(encoder.encode("123"));
                user.setRoles(Set.of(role));
                userRepository.save(user);
            }
        );

    }
}
