package com.huhoot.converter;

import com.huhoot.dto.StudentInChallengeResponse;
import com.huhoot.model.Student;
import com.huhoot.model.StudentInChallenge;

public class StudentInChallengeConverter {

    public static StudentInChallengeResponse toStudentChallengeResponse(StudentInChallenge entity){
        StudentInChallengeResponse response = new StudentInChallengeResponse();

        Student student = entity.getStudent();

        response.setStudentId(student.getId());
        response.setStudentUsername(student.getUsername());
        response.setStudentFullName(student.getFullName());
        response.setIsLogin(entity.isLogin());
        response.setIsKicked(entity.isKicked());
        response.setIsOnline(entity.isOnline());
        return response;
    }
}
