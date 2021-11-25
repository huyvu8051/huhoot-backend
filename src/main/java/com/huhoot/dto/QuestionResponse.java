package com.huhoot.dto;

import com.huhoot.enums.AnswerTime;
import com.huhoot.enums.Points;
import com.huhoot.exception.AnswerOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private int id;
    private int ordinalNumber;
    private String questionContent;
    private AnswerTime answerTimeLimit;
    private Points point;
    private AnswerOption answerOption;
    private Date askDate;
    private boolean isNonDeleted;
}
