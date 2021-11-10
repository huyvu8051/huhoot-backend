package com.huhoot.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class HostAddRequest {
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 5, max = 15)
    private String username;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 5, max = 15)
    private String password;

    public HostAddRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
