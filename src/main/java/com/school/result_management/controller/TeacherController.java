package com.school.result_management.controller;

import com.school.result_management.model.Teacher;
import com.school.result_management.repository.*;
import com.school.result_management.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.school.result_management.model.Subject;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired private TeacherRepository teacherRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private ResultService resultService;
    @Autowired private ResultRepository resultRepository;

    // Helper — gets logged in teacher from security context
    private Teacher getLoggedInTeacher(Authentication auth) {
        String email = auth.getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
        return teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }


    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Teacher teacher = getLoggedInTeacher(auth);
        List<Subject> subjects = subjectRepository.findByTeacherId(teacher.getId());

        // Count total students across all subjects
        int totalStudents = subjects.stream()
                .mapToInt(s -> studentRepository
                        .findByClassRoomId(s.getClassRoom().getId()).size())
                .sum();

        // Count total marks entered
        int totalMarksEntered = subjects.stream()
                .mapToInt(s -> resultRepository
                        .findBySubjectId(s.getId()).size())
                .sum();

        model.addAttribute("teacher", teacher);
        model.addAttribute("subjects", subjects);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalMarksEntered", totalMarksEntered);
        return "teacher/dashboard";
    }

    @GetMapping("/marks/{subjectId}")
    public String enterMarks(@PathVariable Long subjectId, Model model) {
        model.addAttribute("subject", subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found")));
        model.addAttribute("students", studentRepository
                .findByClassRoomId(subjectRepository.findById(subjectId)
                        .get().getClassRoom().getId()));
        model.addAttribute("results", resultService.getResultsBySubject(subjectId));
        return "teacher/marks";
    }

    @PostMapping("/marks/save")
    public String saveMarks(@RequestParam Long studentId,
                            @RequestParam Long subjectId,
                            @RequestParam int marksObtained) {
        resultService.saveResult(studentId, subjectId, marksObtained);
        return "redirect:/teacher/marks/" + subjectId;
    }
}
