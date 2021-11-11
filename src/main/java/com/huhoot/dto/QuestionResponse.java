package com.huhoot.dto;

import com.huhoot.enums.AnswerTime;
import com.huhoot.enums.Points;
import com.huhoot.exception.AnswerOption;
import lombok.Data;

import java.util.Date;

@Data
public class QuestionResponse {
    private int id;
    private int ordinalNumber;
    private String questionContent;

    private AnswerTime answerTimeLimit;

    private Points point;

    private AnswerOption answerOption;

    private Date askDate;

    private boolean isDeleted;
}
