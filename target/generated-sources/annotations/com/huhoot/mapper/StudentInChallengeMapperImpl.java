package com.huhoot.mapper;

import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeUpdateRequest;
import com.huhoot.model.StudentInChallenge;
import java.util.Date;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-01-23T22:12:10+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.1 (Private Build)"
)
public class StudentInChallengeMapperImpl implements StudentInChallengeMapper {

    @Override
    public StudentInChallengeResponse toDto(StudentInChallenge entity) {
        if ( entity == null ) {
            return null;
        }

        int studentId = 0;
        String studentUsername = null;
        String studentFullName = null;
        Boolean isLogin = null;
        Boolean isKicked = null;
        Boolean isOnline = null;
        String createdBy = null;
        Date createdDate = null;
        String modifiedBy = null;
        Date modifiedDate = null;
        Boolean isNonDeleted = null;

        StudentInChallengeResponse studentInChallengeResponse = new StudentInChallengeResponse( studentId, studentUsername, studentFullName, isLogin, isKicked, isOnline, createdBy, createdDate, modifiedBy, modifiedDate, isNonDeleted );

        return studentInChallengeResponse;
    }

    @Override
    public void update(StudentInChallengeUpdateRequest dto, StudentInChallenge entity) {
        if ( dto == null ) {
            return;
        }
    }
}
