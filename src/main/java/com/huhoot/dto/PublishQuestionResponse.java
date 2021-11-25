package com.huhoot.dto;

import com.huhoot.enums.Points;
import com.huhoot.exception.AnswerOption;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
public class PublishQuestionResponse {
    private int id;
    private int ordinalNumber;
    private String questionContent;
    private int answerTimeLimit;

    private Timestamp askDate;

    private Points point;

    private AnswerOption answerOption;

    private List<PublishAnswerResponse> publishAnswerResponses;

    private int challengeId;

    public PublishQuestionResponse(int id, int ordinalNumber, String questionContent, int answerTimeLimit, Points point, AnswerOption answerOption, int challengeId) {
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.questionContent = questionContent;
        this.answerTimeLimit = answerTimeLimit;

        this.point = point;
        this.answerOption = answerOption;
        this.challengeId = challengeId;
    }

}
