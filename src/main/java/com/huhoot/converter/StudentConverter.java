package com.huhoot.converter;

import com.huhoot.dto.PageResponse;
import com.huhoot.dto.StudentDetailsResponse;
import com.huhoot.dto.StudentResponse;
import com.huhoot.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StudentConverter {
    public PageResponse<StudentResponse> toListStudentResponse(Page<Student> page) {
        PageResponse<StudentResponse> result = new PageResponse<>();
        for (Student entity : page) {
            result.getList().add(toStudentResponse(entity));
        }
        result.setTotalElements(page.getTotalElements());
        return result;
    }

    public StudentResponse toStudentResponse(Student entity) {
        StudentResponse result = new StudentResponse();

        result.setId(entity.getId());
        result.setUsername(entity.getUsername());
        result.setFullName(entity.getFullName());
        result.setNonLocked(entity.isNonLocked());
        result.setCreatedDate(entity.getCreatedDate());
        result.setModifiedDate(entity.getModifiedDate());
        return result;
    }

    public StudentDetailsResponse toStudentDetailsResponse(Student entity) {
        StudentDetailsResponse result = new StudentDetailsResponse();

        result.setId(entity.getId());
        result.setUsername(entity.getUsername());
        result.setFullName(entity.getFullName());
        result.setNonLocked(entity.isNonLocked());
        result.setCreatedDate(entity.getCreatedDate());
        result.setModifiedDate(entity.getModifiedDate());
        return result;
    }
}
