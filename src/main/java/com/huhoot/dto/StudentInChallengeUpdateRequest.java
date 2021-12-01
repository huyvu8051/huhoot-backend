package com.huhoot.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentInChallengeUpdateRequest {
    private int studentId;
    private int challengeId;
    private Boolean isKicked;
    private Boolean isNonDeleted;
}
