package com.huhoot.dto;

import lombok.Data;

@Data
public class RegisterHostRequest {
    private int challengeId;
    private String token;
}
