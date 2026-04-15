package com.school.result_management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "classrooms")
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;          // e.g. "Class 10"

    @Column(nullable = false)
    private String section;       // e.g. "A"

    @Column(nullable = false)
    private String academicYear;  // e.g. "2024-25"

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private List<Student> students;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private List<Subject> subjects;
}