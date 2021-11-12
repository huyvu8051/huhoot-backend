package com.huhoot.dto;

import lombok.Data;

@Data
public class AnswerAddRequest {

    private int questionId;

    private int ordinalNumber;

    private String answerContent;

    private boolean isCorrect;
}
