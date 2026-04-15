package com.school.result_management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)  // FK column in students table
    private User user;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private ClassRoom classRoom;

    @Column(nullable = false, unique = true)
    private String rollNumber;

    private String phone;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Result> results;
}