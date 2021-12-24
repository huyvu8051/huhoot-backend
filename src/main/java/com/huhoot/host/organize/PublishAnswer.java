package com.huhoot.host.organize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishAnswer {
    private int id;
    private int ordinalNumber;
    private String answerContent;
    private Boolean isCorrect;
    private Long createdDate;
    private boolean isSelected;

    public PublishAnswer(int id, int ordinalNumber, String answerContent, boolean isCorrect){
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.answerContent = answerContent;
        this.isCorrect = isCorrect;
    }
    public PublishAnswer(int id, int ordinalNumber, String answerContent){
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.answerContent = answerContent;
    }
}