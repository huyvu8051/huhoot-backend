package com.huhoot.organize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class AnswerResultResponse {
    private int id;
    private int ordinalNumber;
    private String content;
    private Boolean isCorrect;
    private int questionId;

    public AnswerResultResponse(int id, int ordinalNumber, String content) {
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.content = content;
    }

    public AnswerResultResponse(int answerId, int answerOrdinalNumber, String content,  int questionId) {
        this.id = answerId;
        this.ordinalNumber = answerOrdinalNumber;
        this.content = content;
        this.questionId = questionId;
    }

    public AnswerResultResponse(int id, int ordinalNumber, String content,  Boolean isCorrect, int questionId) {
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.content = content;
        this.isCorrect = isCorrect;
        this.questionId = questionId;
    }
}
