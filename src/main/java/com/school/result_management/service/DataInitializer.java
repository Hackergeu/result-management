package com.school.result_management.service;

import com.school.result_management.model.Role;
import com.school.result_management.model.User;
import com.school.result_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("vanshagarwal953@gmail.com").isEmpty()) {
            User admin = new User();
            admin.setName("Vansh Agarwal");
            admin.setEmail("vanshagarwal953@gmail.com");
            admin.setPassword(passwordEncoder.encode("HackerGeu"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            System.out.println("====================================");
            System.out.println("Admin user created successfully");
            System.out.println("====================================");
        } else {
            System.out.println("====================================");
            System.out.println("Admin already exists — skipping");
            System.out.println("====================================");
        }
    }
}
