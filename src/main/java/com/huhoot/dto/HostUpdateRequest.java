package com.huhoot.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class HostUpdateRequest {
    private int id;
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 5, max = 15)
    private String password;
    private boolean isNonLocked;
}
