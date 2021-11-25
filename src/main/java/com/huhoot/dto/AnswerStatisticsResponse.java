package com.huhoot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerStatisticsResponse {
    private int answerId;
    private int answerOrdinalNumber;
    private String answerContent;
    private long numberOfStudentSelect;
    private int questionId;

}
