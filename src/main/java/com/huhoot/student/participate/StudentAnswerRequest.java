package com.huhoot.student.participate;

import lombok.Data;

import java.util.List;

@Data
public class StudentAnswerRequest {
    private List<Integer> answerIds;
    private int challengeId;
    private int questionId;
    private String hashCorrectAnswerIds;
    private String adminSocketId;
}
