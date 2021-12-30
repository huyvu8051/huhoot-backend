package com.huhoot.host.manage.question;

import com.huhoot.enums.AnswerOption;
import com.huhoot.enums.Points;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class QuestionAddRequest {

    private int challengeId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String questionContent;

    private String questionImage;

    @NotNull
    @Min(5)
    private int answerTimeLimit;

    @NotNull
    private Points point;

    @NotNull
    private AnswerOption answerOption;
}
