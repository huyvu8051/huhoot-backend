package com.huhoot.dto;

import com.huhoot.enums.Points;
import com.huhoot.exception.AnswerOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private int id;
    private int ordinalNumber;
    private String questionContent;
    private String questionImage;
    private int answerTimeLimit;
    private Points point;
    private AnswerOption answerOption;
    private Date askDate;
    private boolean isNonDeleted;
    private Timestamp createdDate;
    public QuestionResponse(int id, int ordinalNumber, String questionContent, String questionImage, int answerTimeLimit, Points point, AnswerOption answerOption, Date askDate, boolean isNonDeleted ){
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.questionContent = questionContent;
        this.questionImage = questionImage;
        this.answerTimeLimit = answerTimeLimit;
        this.point = point;
        this.answerOption = answerOption;
        this.askDate = askDate;
        this.isNonDeleted = isNonDeleted;
    }
}
