package com.huhoot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishAnswerResponse {
    private int id;
    private int ordinalNumber;
    private String answerContent;
}
