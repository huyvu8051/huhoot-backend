package com.huhoot.host.manage.studentInChallenge;

import lombok.Data;

@Data
public class StudentInChallengeUpdateRequest {
    private int studentId;
    private int challengeId;
    private Boolean isKicked;
    private Boolean isNonDeleted;
}
