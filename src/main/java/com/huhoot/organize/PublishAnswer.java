package com.huhoot.organize;

import com.huhoot.auditing.AuditableDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishAnswer extends AuditableDto {
    private int id;
    private int ordinalNumber;
    private String answerContent;
    private Boolean isCorrect;
    private boolean isSelected;

    public PublishAnswer(int id, int ordinalNumber, String answerContent, Boolean isCorrect,
                         boolean isSelected, Date createdDate, String createdBy, Date modifiedDate, String modifiedBy) {
        super(createdDate, createdBy, modifiedDate, modifiedBy);
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.answerContent = answerContent;
        this.isCorrect = isCorrect;
        this.isSelected = isSelected;
    }

    public PublishAnswer(int id, int ordinalNumber, String answerContent, boolean isCorrect) {
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.answerContent = answerContent;
        this.isCorrect = isCorrect;
    }

    public PublishAnswer(int id, int ordinalNumber, String answerContent) {
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.answerContent = answerContent;
    }
}
