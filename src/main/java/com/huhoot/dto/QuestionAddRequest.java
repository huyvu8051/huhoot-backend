package com.huhoot.dto;

import com.huhoot.enums.AnswerTime;
import com.huhoot.enums.Points;
import com.huhoot.exception.AnswerOption;
import lombok.Data;

@Data
public class QuestionAddRequest {

    private int challengeId;

    private int ordinalNumber;

    private String questionContent;

    private AnswerTime answerTimeLimit;

    private Points point;

    private AnswerOption answerOption;
}
