package com.huhoot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResultResponse {
    private int id;
    private int ordinalNumber;
    private String content;
    private long numberOfStudentSelect;
    private Boolean isCorrect;
    private int questionId;
    private boolean isSelected;

    public AnswerResultResponse(int id, int ordinalNumber, String content) {
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.content = content;
    }

    public AnswerResultResponse(int answerId, int answerOrdinalNumber, String content, long numberOfStudentSelect, int questionId) {
        this.id = answerId;
        this.ordinalNumber = answerOrdinalNumber;
        this.content = content;
        this.numberOfStudentSelect = numberOfStudentSelect;
        this.questionId = questionId;
    }

    public AnswerResultResponse(int id, int ordinalNumber, String content, long numberOfStudentSelect, Boolean isCorrect, int questionId) {
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.content = content;
        this.numberOfStudentSelect = numberOfStudentSelect;
        this.isCorrect = isCorrect;
        this.questionId = questionId;
    }
}
