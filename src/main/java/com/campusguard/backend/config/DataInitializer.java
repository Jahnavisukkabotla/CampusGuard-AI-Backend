package com.campusguard.backend.config;

import com.campusguard.backend.entity.User;
import com.campusguard.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository repository, PasswordEncoder encoder) {
        return args -> {

            System.out.println("=== DataInitializer Started ===");

            User user = repository.findByEmail("jahnavi2108@gmail.com")
                    .orElse(new User());

            user.setFullName("Jahnavi");
            user.setEmail("jahnavi2108@gmail.com");
            user.setPassword(encoder.encode("123456"));
            user.setRole("USER");

            repository.save(user);

            System.out.println("Default user created/updated.");
        };
    }
}