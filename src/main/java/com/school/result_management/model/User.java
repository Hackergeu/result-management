package com.school.result_management.model;

import jakarta.persistence.*;
import lombok.Data;

@Data                          // Lombok: auto-generates getters, setters, toString
@Entity                        // Tells JPA: map this class to a database table
@Table(name = "users")         // Table name is "users"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment ID
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)  // No two users can have the same email
    private String email;

    @Column(nullable = false)
    private String password;   // Will store BCrypt-hashed password, never plain text

    @Enumerated(EnumType.STRING)  // Stores "ADMIN"/"TEACHER"/"STUDENT" as text in DB
    @Column(nullable = false)
    private Role role;
}