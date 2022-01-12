package com.huhoot.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthenticationControllerTest {
    @Autowired
    private AuthenticationController controller;

    @BeforeEach
    void setUp(){

    }

    @Test
    void correctAuth(){
        AuthenticationResponse body = getAuth("admin", "password");
        assertNotNull(body.getJwt());
        assertEquals("admin", body.getUsername());
        Collection<? extends GrantedAuthority> authorities = body.getAuthorities();
        assertTrue(authorities.stream().anyMatch(e -> e.getAuthority().equals("ADMIN")));
    }

    @Test
    void incorrectAuthorities(){
        AuthenticationResponse body = getAuth("admin0", "password");
        Collection<? extends GrantedAuthority> authorities = body.getAuthorities();
        assertFalse(authorities.stream().anyMatch(e -> e.getAuthority().equals("ADMIN")));
    }

    @Test
    void incorrectPassword(){
        assertThrows(BadCredentialsException.class, ()->{
            getAuth("admin", "incorrectPassword");
        });
    }

    private AuthenticationResponse getAuth(String username, String password) {
        AuthenticationRequest request = new AuthenticationRequest().builder()
                .username(username)
                .password(password)
                .build();
        ResponseEntity<AuthenticationResponse> response = controller.createAuthenticationToken(request);
        return response.getBody();
    }
}