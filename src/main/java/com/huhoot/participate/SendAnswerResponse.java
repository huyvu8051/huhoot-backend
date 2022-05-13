package com.huhoot.participate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendAnswerResponse {
    private String pointsReceived;
    private String combo;
    private String comboToken;
    private String answerResultToken;

}
