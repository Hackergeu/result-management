package com.school.result_management.controller;

import com.school.result_management.model.*;
import com.school.result_management.repository.*;
import com.school.result_management.service.AdminService;
import com.school.result_management.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private AdminService adminService;
    @Autowired private ClassRoomRepository classRoomRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private PdfService pdfService;

    // ── DASHBOARD ──────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Student> allStudents = adminService.getAllStudents();

        model.addAttribute("totalStudents", allStudents.size());
        model.addAttribute("totalTeachers", adminService.getAllTeachers().size());
        model.addAttribute("totalClasses", adminService.getAllClasses().size());
        model.addAttribute("totalSubjects", adminService.getAllSubjects().size());

        // Show last 5 students on dashboard
        List<Student> recentStudents = allStudents.stream()
                .skip(Math.max(0, allStudents.size() - 5))
                .collect(java.util.stream.Collectors.toList());

        model.addAttribute("recentStudents", recentStudents);
        return "admin/dashboard";
    }

    // ── CLASSROOM ──────────────────────────────
    @GetMapping("/classes")
    public String classes(Model model) {
        model.addAttribute("classes", adminService.getAllClasses());
        model.addAttribute("classRoom", new ClassRoom());
        return "admin/classes";
    }

    @PostMapping("/classes/save")
    public String saveClass(@ModelAttribute ClassRoom classRoom) {
        adminService.saveClassRoom(classRoom);
        return "redirect:/admin/classes";
    }

    @GetMapping("/classes/delete/{id}")
    public String deleteClass(@PathVariable Long id) {
        adminService.deleteClassRoom(id);
        return "redirect:/admin/classes";
    }

    // ── TEACHER ────────────────────────────────
    @GetMapping("/teachers")
    public String teachers(Model model) {
        model.addAttribute("teachers", adminService.getAllTeachers());
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("user", new User());
        return "admin/teachers";
    }

    @PostMapping("/teachers/save")
    public String saveTeacher(@ModelAttribute Teacher teacher,
                              @ModelAttribute User user) {
        adminService.saveTeacher(teacher, user);
        return "redirect:/admin/teachers";
    }

    @GetMapping("/teachers/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        adminService.deleteTeacher(id);
        return "redirect:/admin/teachers";
    }

    // ── STUDENT ────────────────────────────────
    @GetMapping("/students")
    public String students(Model model) {
        model.addAttribute("students", adminService.getAllStudents());
        model.addAttribute("student", new Student());
        model.addAttribute("user", new User());
        model.addAttribute("classes", adminService.getAllClasses());
        return "admin/students";
    }

    @PostMapping("/students/save")
    public String saveStudent(@ModelAttribute Student student,
                              @ModelAttribute User user,
                              @RequestParam Long classRoomId) {
        adminService.saveStudent(student, user, classRoomId);
        return "redirect:/admin/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        adminService.deleteStudent(id);
        return "redirect:/admin/students";
    }

    // ── SUBJECT ────────────────────────────────
    @GetMapping("/subjects")
    public String subjects(Model model) {
        model.addAttribute("subjects", adminService.getAllSubjects());
        model.addAttribute("subject", new Subject());
        model.addAttribute("classes", adminService.getAllClasses());
        model.addAttribute("teachers", adminService.getAllTeachers());
        return "admin/subjects";
    }

    @PostMapping("/subjects/save")
    public String saveSubject(@ModelAttribute Subject subject,
                              @RequestParam Long classRoomId,
                              @RequestParam Long teacherId) {
        adminService.saveSubject(subject, classRoomId, teacherId);
        return "redirect:/admin/subjects";
    }

    @GetMapping("/subjects/delete/{id}")
    public String deleteSubject(@PathVariable Long id) {
        adminService.deleteSubject(id);
        return "redirect:/admin/subjects";
    }

    // ── PDF MARKSHEET ───────────────────────────
    @GetMapping("/marksheet/{studentId}")
    public ResponseEntity<byte[]> downloadMarksheet(@PathVariable Long studentId) {
        byte[] pdf = pdfService.generateMarksheet(studentId);
        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=marksheet_" + studentId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}