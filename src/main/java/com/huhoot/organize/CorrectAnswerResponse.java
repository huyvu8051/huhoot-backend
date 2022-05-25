package com.huhoot.organize;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CorrectAnswerResponse {
    private List<Integer> answers;
    private String encryptKey;
    private long timeout;
    private int totalStudent;
    private int totalStudentCorrect;
    private int totalStudentWrong;


}
