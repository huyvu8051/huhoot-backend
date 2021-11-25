package com.huhoot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {
    private int id;

    private int ordinalNumber;

    private String answerContent;

    private boolean isCorrect;

}
