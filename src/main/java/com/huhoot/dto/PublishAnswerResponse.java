package com.huhoot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishAnswerResponse {
    private int id;
    private int ordinalNumber;
    private String answerContent;
}
