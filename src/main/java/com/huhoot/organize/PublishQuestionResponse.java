package com.huhoot.organize;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PublishQuestionResponse {
    private String questionToken;
    private PublishQuestion question;
    private List<AnswerResultResponse> answers;
    private String hashCorrectAnswerIds;
    private String adminSocketId;

}
