package com.huhoot.socket;

import lombok.Data;

@Data
public class SocketAuthorizationRequest {
    private int challengeId;
    private String token;
}
