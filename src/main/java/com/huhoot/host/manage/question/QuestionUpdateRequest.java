package com.huhoot.host.manage.question;

import com.huhoot.enums.AnswerOption;
import com.huhoot.enums.Points;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
