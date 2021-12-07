package com.huhoot.dto;

import com.huhoot.enums.Points;
import com.huhoot.enums.AnswerOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublishQuestion {
    private int id;
    private int ordinalNumber;
    private String questionContent;
    private String questionImage;
    private int answerTimeLimit;

    private Timestamp askDate;

    private Points point;

    private AnswerOption answerOption;

    private int challengeId;

    private int totalQuestion;

    private int questionOrder;

    private boolean theLastQuestion;

    public PublishQuestion(int id, int ordinalNumber, String questionContent, String questionImage, int answerTimeLimit, Points point, AnswerOption answerOption, int challengeId,int totalQuestion) {
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.questionContent = questionContent;
        this.answerTimeLimit = answerTimeLimit;

        this.questionImage = questionImage;
        this.point = point;
        this.answerOption = answerOption;
        this.challengeId = challengeId;
        this.totalQuestion = totalQuestion;
    }

}
