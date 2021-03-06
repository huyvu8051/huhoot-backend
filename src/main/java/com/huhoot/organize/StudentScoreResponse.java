package com.huhoot.organize;

import lombok.Data;

@Data
public class StudentScoreResponse {
    private int studentId;
    private String username;
    private String studentFullName;
    private double score;
    private int rank;

    public StudentScoreResponse(int studentId, double score, String studentFullName, String username){
        this.studentId = studentId;
        this.score = score;
        this.studentFullName = studentFullName;
        this.username = username;
    }
}
