package com.huhoot.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordRequest {

    private String oldPassword;

    @Valid
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 5, max = 50)
    private String password;
}
