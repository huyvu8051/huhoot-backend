package com.huhoot.converter;

import com.huhoot.dto.StudentResponse;
import com.huhoot.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StudentConverter {

    public static StudentResponse toStudentResponse(Student entity) {
        StudentResponse result = new StudentResponse();

        result.setId(entity.getId());
        result.setUsername(entity.getUsername());
        result.setFullName(entity.getFullName());
        result.setNonLocked(entity.isNonLocked());
        result.setCreatedDate(entity.getCreatedDate());
        result.setModifiedDate(entity.getModifiedDate());
        return result;
    }

}
