package com.huhoot.mapper.impl;

import com.huhoot.admin.manage.student.StudentAddRequest;
import com.huhoot.mapper.StudentMapper;
import com.huhoot.dto.StudentResponse;
import com.huhoot.admin.manage.student.StudentUpdateRequest;
import com.huhoot.auditing.AuditingMapperImpl;
import com.huhoot.model.Student;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class StudentMapperImpl implements StudentMapper {

    private final AuditingMapperImpl auditingMapper;

    @Override
    public StudentResponse toDto(Student entity) {
        if (entity == null) {
            return null;
        }

        StudentResponse response = new StudentResponse();

        auditingMapper.setAudit(entity, response);

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
