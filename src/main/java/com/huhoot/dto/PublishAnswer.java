package com.huhoot.dto;

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
    private boolean isCorrect;

    public PublishAnswer(int id, int ordinalNumber, String answerContent){
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.answerContent = answerContent;
    }
}
