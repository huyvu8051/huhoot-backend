package com.huhoot.dto;

import lombok.Data;

@Data
public class SocketAuthorizationRequest {
    private int challengeId;
    private String token;
}
