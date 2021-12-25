package com.huhoot.host.manage.question;

import com.huhoot.auditing.AuditableDto;
import com.huhoot.enums.AnswerOption;
import com.huhoot.enums.Points;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
public class QuestionResponse extends AuditableDto {
    private int id;
    private int ordinalNumber;
    private String questionContent;
    private String questionImage;
    private int answerTimeLimit;
    private Points point;
    private AnswerOption answerOption;
    private Long askDate;
    private boolean isNonDeleted;

    public QuestionResponse() {
        super();
    }

    public QuestionResponse(int id, int ordinalNumber, String questionContent, String questionImage,
                            int answerTimeLimit, Points point, AnswerOption answerOption, Date askDate,
                            boolean isNonDeleted, Date createdDate, String createdBy, Date modifiedDate, String modifiedBy) {
        super(createdDate, createdBy, modifiedDate, modifiedBy);
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.questionContent = questionContent;
        this.questionImage = questionImage;
        this.answerTimeLimit = answerTimeLimit;
        this.point = point;
        this.answerOption = answerOption;
        if(askDate != null){
            this.askDate = askDate.getTime();
        }
        this.isNonDeleted = isNonDeleted;
    }

    public QuestionResponse(int id, int ordinalNumber, String questionContent, String questionImage, int answerTimeLimit, Points point, AnswerOption answerOption, Date askDate, boolean isNonDeleted ){
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.questionContent = questionContent;
        this.questionImage = questionImage;
        this.answerTimeLimit = answerTimeLimit;
        this.point = point;
        this.answerOption = answerOption;
        if(askDate != null){
            this.askDate = askDate.getTime();
        }
        this.isNonDeleted = isNonDeleted;
    }
}
