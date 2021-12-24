package com.huhoot.mapper.impl;

import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeUpdateRequest;
import com.huhoot.mapper.StudentInChallengeMapper;
import com.huhoot.model.Student;
import com.huhoot.model.StudentInChallenge;
import com.huhoot.model.StudentInChallengeId;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-11-30T23:13:30+0700",
        comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (Temurin)"
)
@Primary
@Component("myStudentInChallengeMapper")
public class StudentInChallengeMapperImpl implements StudentInChallengeMapper {

    @Override
    public StudentInChallengeResponse toDto(StudentInChallenge entity) {
        if (entity == null) {
            return null;
        }

        StudentInChallengeResponse studentInChallengeResponse = new StudentInChallengeResponse();

        studentInChallengeResponse.setStudentId(entityPrimaryKeyStudentId(entity));
        studentInChallengeResponse.setStudentUsername(entityPrimaryKeyStudentUsername(entity));
        studentInChallengeResponse.setStudentFullName(entityPrimaryKeyStudentFullName(entity));
        studentInChallengeResponse.setIsLogin(entity.isLogin());
        studentInChallengeResponse.setIsKicked(entity.isKicked());
        studentInChallengeResponse.setIsOnline(entity.isOnline());
        studentInChallengeResponse.setCreatedBy(entity.getCreatedBy());
        studentInChallengeResponse.setCreatedDate(entity.getCreatedDate().getTime());
        studentInChallengeResponse.setModifiedBy(entity.getModifiedBy());
        studentInChallengeResponse.setModifiedDate(entity.getModifiedDate().getTime());
        studentInChallengeResponse.setIsNonDeleted(entity.isNonDeleted());

        return studentInChallengeResponse;
    }

    @Override
    public void update(StudentInChallengeUpdateRequest dto, StudentInChallenge entity) {
        if (dto == null) {
            return;
        }

        if (dto.getIsNonDeleted() != null) {
            entity.setNonDeleted(dto.getIsNonDeleted());
        }
        if (dto.getIsKicked() != null) {
            entity.setKicked(dto.getIsKicked());
        }
    }

    private int entityPrimaryKeyStudentId(StudentInChallenge studentInChallenge) {
        if (studentInChallenge == null) {
            return 0;
        }
        StudentInChallengeId primaryKey = studentInChallenge.getPrimaryKey();
        if (primaryKey == null) {
            return 0;
        }
        Student student = primaryKey.getStudent();
        if (student == null) {
            return 0;
        }
        int id = student.getId();
        return id;
    }

    private String entityPrimaryKeyStudentUsername(StudentInChallenge studentInChallenge) {
        if (studentInChallenge == null) {
            return null;
        }
        StudentInChallengeId primaryKey = studentInChallenge.getPrimaryKey();
        if (primaryKey == null) {
            return null;
        }
        Student student = primaryKey.getStudent();
        if (student == null) {
            return null;
        }
        String username = student.getUsername();
        if (username == null) {
            return null;
        }
        return username;
    }

    private String entityPrimaryKeyStudentFullName(StudentInChallenge studentInChallenge) {
        if (studentInChallenge == null) {
            return null;
        }
        StudentInChallengeId primaryKey = studentInChallenge.getPrimaryKey();
        if (primaryKey == null) {
            return null;
        }
        Student student = primaryKey.getStudent();
        if (student == null) {
            return null;
        }
        String fullName = student.getFullName();
        if (fullName == null) {
            return null;
        }
        return fullName;
    }
}
