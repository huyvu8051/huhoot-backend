package com.huhoot.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentInChallengeAddRequest {
    private int challengeId;
    private List<Integer> studentIds;
}
