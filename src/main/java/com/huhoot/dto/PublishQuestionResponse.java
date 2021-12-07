package com.huhoot.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PublishQuestionResponse {
    private PublishQuestion question;
    private List<AnswerResultResponse> answers;
}
