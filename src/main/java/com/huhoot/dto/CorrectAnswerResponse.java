package com.huhoot.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CorrectAnswerResponse {
    private List<PublishAnswer> answers;
    private List<AnswerStatisticsResponse> answerStatistics;

    private String encryptKey;

}
