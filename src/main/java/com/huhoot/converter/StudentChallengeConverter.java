package com.huhoot.converter;

import com.google.common.collect.Interners;
import com.huhoot.dto.StudentInChallengeResponse;
import com.huhoot.model.Student;
import com.huhoot.model.StudentChallenge;

public class StudentChallengeConverter {

    public static StudentInChallengeResponse toStudentChallengeResponse(StudentChallenge entity){
        StudentInChallengeResponse response = new StudentInChallengeResponse();

        Student student = entity.getStudent();

        response.setStudentId(student.getId());
        response.setChallengeId(entity.getChallenge().getId());
        response.setStudentUsername(student.getUsername());
        response.setStudentFullname(student.getFullName());
        response.setLogin(entity.isLogin());
        response.setKicked(entity.isKicked());
        response.setOnline(entity.isOnline());
        return response;
    }
}
