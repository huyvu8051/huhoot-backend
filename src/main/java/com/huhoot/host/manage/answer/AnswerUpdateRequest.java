package com.huhoot.host.manage.answer;

import lombok.Data;

@Data
public class AnswerUpdateRequest {

    private int id;
    private Integer ordinalNumber;
    private String answerContent;
    private Boolean isCorrect;
    private Boolean isNonDeleted;
}
