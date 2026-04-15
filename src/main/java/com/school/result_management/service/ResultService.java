package com.school.result_management.service;

import com.school.result_management.model.*;
import com.school.result_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ResultService {

    @Autowired private ResultRepository resultRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private SubjectRepository subjectRepository;

    // Called by teacher to save/update marks
    public void saveResult(Long studentId, Long subjectId, int marksObtained) {

        // Check if result already exists for this student+subject
        Result result = resultRepository
                .findByStudentIdAndSubjectId(studentId, subjectId)
                .orElse(new Result());

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        result.setStudent(student);
        result.setSubject(subject);
        result.setMarksObtained(marksObtained);
        result.setGrade(calculateGrade(marksObtained, subject.getMaxMarks()));
        result.setIsPassed(marksObtained >= subject.getPassingMarks());

        resultRepository.save(result);
    }

    // Grade calculation logic
    private String calculateGrade(int marks, int maxMarks) {
        double percentage = (marks * 100.0) / maxMarks;
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        return "F";
    }

    public List<Result> getResultsByStudent(Long studentId) {
        return resultRepository.findByStudentId(studentId);
    }

    public List<Result> getResultsBySubject(Long subjectId) {
        return resultRepository.findBySubjectId(subjectId);
    }
}