package com.huhoot.student.participate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendAnswerResponse {
    private String isCorrect;
    private String totalPoint;
}
