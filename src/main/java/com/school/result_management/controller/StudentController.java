package com.school.result_management.controller;

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

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired private StudentRepository studentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ResultService resultService;
    @Autowired private PdfService pdfService;

    // Helper — gets the logged in student from security context
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
        model.addAttribute("student", student);
        model.addAttribute("results", resultService.getResultsByStudent(student.getId()));
        return "student/dashboard";
    }

    @GetMapping("/marksheet/download")
    public ResponseEntity<byte[]> downloadMarksheet(Authentication auth) {
        Student student = getLoggedInStudent(auth);
        byte[] pdf = pdfService.generateMarksheet(student.getId());

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=marksheet_" + student.getRollNumber() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}