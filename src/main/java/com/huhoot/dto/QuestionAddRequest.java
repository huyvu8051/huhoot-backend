package com.huhoot.dto;

import com.huhoot.enums.AnswerTime;
import com.huhoot.enums.Points;
import com.huhoot.exception.AnswerOption;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class QuestionAddRequest {

    private int challengeId;

    private int ordinalNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String questionContent;

    @NotNull
    private AnswerTime answerTimeLimit;

    @NotNull
    private Points point;

    @NotNull
    private AnswerOption answerOption;
}
