package com.huhoot.dto;

import com.huhoot.enums.Points;
import com.huhoot.exception.AnswerOption;
import lombok.Data;

@Data
public class QuestionUpdateRequest {
    private int id;
    private Integer ordinalNumber;
    private String questionContent;

    private String questionImage;

    private Integer answerTimeLimit;
    private Points point;
    private AnswerOption answerOption;
    private Boolean isNonDeleted;
}
