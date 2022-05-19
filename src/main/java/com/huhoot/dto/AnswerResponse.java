package com.huhoot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerResponse {
    private double pointsReceived;
    private int comboCount;


}
