package com.school.result_management.controller;

import com.school.result_management.model.Result;
import com.school.result_management.model.Student;
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
import java.util.Map;

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
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        List<Student> students = studentRepository
                .findByClassRoomId(subject.getClassRoom().getId());

        List<Result> results = resultService.getResultsBySubject(subjectId);

        // Build a map of studentId -> Result so HTML doesn't need complex expressions
        Map<Long, Result> resultMap = new java.util.HashMap<>();
        for (Result r : results) {
            resultMap.put(r.getStudent().getId(), r);
        }

        model.addAttribute("subject", subject);
        model.addAttribute("students", students);
        model.addAttribute("results", results);
        model.addAttribute("resultMap", resultMap);
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
