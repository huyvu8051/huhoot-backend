package com.huhoot.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AnswerAddRequest {

    private int questionId;

    private int ordinalNumber;
    
    @NotNull
    @NotBlank
    @NotEmpty
    private String answerContent;

    private boolean isCorrect;
}
