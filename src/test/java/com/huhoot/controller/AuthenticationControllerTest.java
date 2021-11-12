package com.huhoot.controller;

import com.huhoot.dto.AuthenticationRequest;
import com.huhoot.dto.AuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {


    @Autowired
    private AuthenticationController controller;

    @Autowired
    private MockMvc mvc;


    @Test
    String createAuthenticationToken() {


        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("Admin");
        request.setPassword("admin");

        ResponseEntity<AuthenticationResponse> response = controller.createAuthenticationToken(request);
        String jwt = response.getBody().getJwt();
        System.out.println(jwt);
        return jwt;


    }

    @Test
    public void shouldGenerateAuthToken() throws Exception {
        String jwt = createAuthenticationToken();

        MvcResult authorization = mvc.perform(MockMvcRequestBuilders.get("/test").header("Authorization", jwt)).andReturn();

    }

}