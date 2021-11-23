package com.huhoot.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentAnswerRequest {
    private List<Integer> answerIds;
    private int challengeId;
    private int questionId;
}
