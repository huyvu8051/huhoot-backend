package com.huhoot.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class StudentScoreResponse {
    private int studentId;
    private String studentFullName;
    private double score;
    private int rank;

    public StudentScoreResponse(int studentId, double score, String studentFullName){
        this.studentId = studentId;
        this.score = score;
        this.studentFullName = studentFullName;
    }
}
