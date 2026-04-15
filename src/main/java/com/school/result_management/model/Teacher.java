package com.school.result_management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String employeeCode;  // e.g. "TCH001"

    private String department;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Subject> subjects;
}