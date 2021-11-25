package com.huhoot.dto;

import com.huhoot.enums.AnswerTime;
import com.huhoot.enums.Points;
import com.huhoot.exception.AnswerOption;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class QuestionUpdateRequest{
    private int id;

    private int ordinalNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String questionContent;

    @NotNull
    @Min(5)
    private int answerTimeLimit;

    @NotNull
    private Points point;

    @NotNull
    private AnswerOption answerOption;
}
