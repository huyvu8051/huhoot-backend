package com.huhoot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class StudentAddRequest {
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 5, max = 10)
    @Pattern(regexp = "^\\d{10}$")
    private String username;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 5, max = 50)
    private String fullName;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 5, max = 15)
    private String password;

    public StudentAddRequest(String username, String password) {
        this.username = username;
        this.password = password;

    }
}
