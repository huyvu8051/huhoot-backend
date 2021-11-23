package com.huhoot;

import com.huhoot.controller.AuthenticationController;
import com.huhoot.dto.AuthenticationRequest;
import com.huhoot.dto.AuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BackendApplicationTests {
    @Autowired
    private AuthenticationController controller;

    @Test
    public void getTokenWithValidAccount() {

        String token = getToken("admin", "password");

        assertNotNull(token);

        String token1 = getToken("admin1", "password");

        assertNotNull(token1);

    }

    @Test
    void getTokenWithErrAccount() {

        assertThrows(BadCredentialsException.class, () -> {
            getToken("err", "password");
        });

        assertThrows(BadCredentialsException.class, () -> {
            getToken("admin", "dfds");
        });

        assertThrows(BadCredentialsException.class, () -> {
            getToken("", "password");
        });

        assertThrows(BadCredentialsException.class, () -> {
            getToken("admin", "");
        });

        assertThrows(BadCredentialsException.class, () -> {
            getToken("", "");
        });



    }




    private String getToken(String username, String password) {
        ResponseEntity<AuthenticationResponse> authenticationToken = controller.createAuthenticationToken(new AuthenticationRequest(username, password));

        System.out.println();
        String jwt = authenticationToken.getBody().getJwt();
        return jwt;
    }
}
