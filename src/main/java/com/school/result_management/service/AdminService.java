package com.school.result_management.service;

import com.school.result_management.model.*;
import com.school.result_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    @Autowired private UserRepository userRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private ClassRoomRepository classRoomRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // ── CLASSROOM ──────────────────────────────
    public List<ClassRoom> getAllClasses() {
        return classRoomRepository.findAll();
    }

    public void saveClassRoom(ClassRoom classRoom) {
        classRoomRepository.save(classRoom);
    }

    public void deleteClassRoom(Long id) {
        classRoomRepository.deleteById(id);
    }

    // ── TEACHER ────────────────────────────────
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public void saveTeacher(Teacher teacher, User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.TEACHER);
        userRepository.save(user);
        teacher.setUser(user);
        teacherRepository.save(teacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    // ── STUDENT ────────────────────────────────
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void saveStudent(Student student, User user, Long classRoomId) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.STUDENT);
        userRepository.save(user);
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        student.setUser(user);
        student.setClassRoom(classRoom);
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // ── SUBJECT ────────────────────────────────
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public void saveSubject(Subject subject, Long classRoomId, Long teacherId) {
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        subject.setClassRoom(classRoom);
        subject.setTeacher(teacher);
        subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}