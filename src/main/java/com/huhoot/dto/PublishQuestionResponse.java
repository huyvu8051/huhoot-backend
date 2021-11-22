package com.huhoot.dto;

import com.huhoot.enums.AnswerTime;
import com.huhoot.enums.Points;
import com.huhoot.exception.AnswerOption;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PublishQuestionResponse {
    private int id;
    private int ordinalNumber;
    private String questionContent;
    private AnswerTime answerTimeLimit;

    private Points point;

    private AnswerOption answerOption;

    private List<PublishAnswerResponse> publishAnswerResponses = new ArrayList<>();
}
