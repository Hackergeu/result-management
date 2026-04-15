package com.school.result_management.repository;

import com.school.result_management.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByClassRoomId(Long classRoomId);
    List<Subject> findByTeacherId(Long teacherId);
}
