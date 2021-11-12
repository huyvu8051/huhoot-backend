package com.huhoot.dto;

import lombok.Data;

@Data
public class AnswerResponse {
    private int id;

    private int ordinalNumber;

    private String answerContent;

    private boolean isCorrect;

}
