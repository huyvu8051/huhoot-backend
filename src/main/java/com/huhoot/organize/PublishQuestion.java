package com.huhoot.organize;

import com.huhoot.enums.AnswerOption;
import com.huhoot.enums.Points;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    private long askDate;

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
    public PublishQuestion(int id, int ordinalNumber, String questionContent, String questionImage, int answerTimeLimit, Points point, Long askDate, AnswerOption answerOption, int challengeId, int totalQuestion) {
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.questionContent = questionContent;
        this.answerTimeLimit = answerTimeLimit;
        this.askDate = askDate;
        this.questionImage = questionImage;
        this.point = point;
        this.answerOption = answerOption;
        this.challengeId = challengeId;
        this.totalQuestion = totalQuestion;
    }

}
