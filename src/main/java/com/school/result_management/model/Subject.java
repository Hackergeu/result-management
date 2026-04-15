package com.school.result_management.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private ClassRoom classRoom;

    @Column(nullable = false)
    private String name;         // e.g. "Mathematics"

    @Column(nullable = false)
    private Integer maxMarks;    // e.g. 100

    @Column(nullable = false)
    private Integer passingMarks; // e.g. 35
}