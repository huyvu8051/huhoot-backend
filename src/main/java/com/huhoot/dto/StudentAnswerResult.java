package com.huhoot.dto;

import com.huhoot.model.Answer;
import com.huhoot.organize.PublishAnswer;
import lombok.Data;

@Data
public class StudentAnswerResult {
    private Double score;
    private Boolean isCorrect;
    private Long answerDate;
    private PublishAnswer answer;
    private int questionId;

    public StudentAnswerResult(Double score, Boolean isCorrect, Long answerDate,int questionId, Answer answerModel) {
        this.score = score;
        this.isCorrect = isCorrect;
        this.answerDate = answerDate;
        this.answer = PublishAnswer.builder()
                .id(answerModel.getId())
                .ordinalNumber(answerModel.getOrdinalNumber())
                .answerContent(answerModel.getAnswerContent())
                .isCorrect(answerModel.isCorrect())
                .build();

        this.questionId = questionId;
    }
}
