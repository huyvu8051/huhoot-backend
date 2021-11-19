package com.huhoot.controller;

import com.huhoot.dto.AuthenticationRequest;
import com.huhoot.dto.AuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class AuthenticationControllerTest {

    @Autowired
    private AuthenticationController controller;

    @Test
    public String getToken() {
        ResponseEntity<AuthenticationResponse> authenticationToken = controller.createAuthenticationToken(new AuthenticationRequest("admin", "admin"));

        return authenticationToken.getBody().getJwt();

    }
}