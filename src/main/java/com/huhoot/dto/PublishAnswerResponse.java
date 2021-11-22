package com.huhoot.dto;

import lombok.Data;

@Data
public class PublishAnswerResponse {
    private int id;
    private int ordinalNumber;
    private String answerContent;
}
