package com.school.result_management.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
        name = "results",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id"})
        // One student can have only one result per subject — enforced at DB level
)
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private Integer marksObtained;

    private String grade;      // Auto-calculated: A, B, C, D, F

    private Boolean isPassed;  // Auto-calculated: marksObtained >= subject.passingMarks
}
