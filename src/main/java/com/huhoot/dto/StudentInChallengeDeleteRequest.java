package com.huhoot.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentInChallengeDeleteRequest {
    private List<Integer> studentIds;
    private int challengeId;
}
