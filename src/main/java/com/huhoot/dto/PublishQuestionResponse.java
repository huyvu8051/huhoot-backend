package com.huhoot.dto;

import com.huhoot.enums.AnswerTime;
import com.huhoot.enums.Points;
import com.huhoot.exception.AnswerOption;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PublishQuestionResponse {
    private int id;
    private int ordinalNumber;
    private String questionContent;
    private int answerTimeLimit;

    private Timestamp askDate;

    private Points point;

    private AnswerOption answerOption;

    private List<PublishAnswerResponse> publishAnswerResponses;
}
