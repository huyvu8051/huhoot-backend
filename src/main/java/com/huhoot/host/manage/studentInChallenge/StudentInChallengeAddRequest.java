package com.huhoot.host.manage.studentInChallenge;

import lombok.Data;

import java.util.List;

@Data
public class StudentInChallengeAddRequest {
    private int challengeId;
    private List<Integer> studentIds;
}
