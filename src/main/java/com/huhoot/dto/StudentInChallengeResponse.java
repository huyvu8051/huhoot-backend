package com.huhoot.dto;

import lombok.Data;

@Data
public class StudentInChallengeResponse {
    private int studentId;
    private int challengeId;
    private String studentUsername;
    private String studentFullName;
    private boolean isLogin;
    private boolean isKicked;
    private boolean isOnline;

}
