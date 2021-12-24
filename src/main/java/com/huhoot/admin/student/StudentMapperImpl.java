package com.huhoot.admin.student;

import com.huhoot.auditing.AuditingDtoMapper;
import com.huhoot.dto.StudentAddRequest;
import com.huhoot.admin.student.StudentResponse;
import com.huhoot.dto.StudentUpdateRequest;
import com.huhoot.admin.student.StudentMapper;
import com.huhoot.model.Student;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-12-02T03:21:25+0700",
        comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (Temurin)"
)
@Primary
@Component("myStudentMapper")
public class StudentMapperImpl extends AuditingDtoMapper<Student, StudentResponse>  implements StudentMapper{

    @Override
    public StudentResponse toDto(Student entity) {
        if (entity == null) {
            return null;
        }

        StudentResponse response = new StudentResponse();

        setAudit(entity, response);

        response.setIsNonLocked(entity.isNonLocked());
        response.setId(entity.getId());
        response.setUsername(entity.getUsername());
        response.setFullName(entity.getFullName());


        return response;
    }

    @Override
    public Student toEntity(StudentAddRequest request) {
        if (request == null) {
            return null;
        }

        Student student = new Student();

        student.setUsername(request.getUsername());
        student.setFullName(request.getFullName());
        student.setNonLocked(request.getIsNonLocked());

        return student;
    }

    @Override
    public void update(StudentUpdateRequest request, Student entity) {
        if (request == null) {
            return;
        }

        if (request.getIsNonLocked() != null) {
            entity.setNonLocked(request.getIsNonLocked());
        }
        entity.setId(request.getId());

        if (request.getUsername() != null) {
            entity.setUsername(request.getUsername());
        }
        if (request.getFullName() != null) {
            entity.setFullName(request.getFullName());
        }
    }
}
