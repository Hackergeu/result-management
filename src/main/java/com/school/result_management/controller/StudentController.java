package com.school.result_management.controller;

import com.school.result_management.model.Result;
import com.school.result_management.model.Student;
import com.school.result_management.repository.StudentRepository;
import com.school.result_management.repository.UserRepository;
import com.school.result_management.service.PdfService;
import com.school.result_management.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired private StudentRepository studentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ResultService resultService;
    @Autowired private PdfService pdfService;

    private Student getLoggedInStudent(Authentication auth) {
        String email = auth.getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Student student = getLoggedInStudent(auth);
        List<Result> results = resultService.getResultsByStudent(student.getId());

        // Calculate all stats in Java — never in Thymeleaf
        long subjectsAppeared = results.size();
        long subjectsPassed   = results.stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsPassed()))
                .count();

        int totalMarksObtained = results.stream()
                .mapToInt(Result::getMarksObtained)
                .sum();
        int totalMaxMarks = results.stream()
                .mapToInt(r -> r.getSubject().getMaxMarks())
                .sum();

        String percentage = totalMaxMarks > 0
                ? String.format("%.1f%%",
                (totalMarksObtained * 100.0) / totalMaxMarks)
                : "0%";

        model.addAttribute("student", student);
        model.addAttribute("results", results);
        model.addAttribute("subjectsAppeared", subjectsAppeared);
        model.addAttribute("subjectsPassed", subjectsPassed);
        model.addAttribute("percentage", percentage);
        return "student/dashboard";
    }

    @GetMapping("/marksheet/download")
    public ResponseEntity<byte[]> downloadMarksheet(Authentication auth) {
        Student student = getLoggedInStudent(auth);
        byte[] pdf = pdfService.generateMarksheet(student.getId());
        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=marksheet_"
                                + student.getRollNumber() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}