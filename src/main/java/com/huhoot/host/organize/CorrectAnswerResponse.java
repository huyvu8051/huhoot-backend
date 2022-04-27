package com.huhoot.host.organize;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CorrectAnswerResponse {
    private List<AnswerResultResponse> answers;
    private String encryptKey;
    private int totalStudent;
    private int totalStudentCorrect;
    private int totalStudentWrong;


}
