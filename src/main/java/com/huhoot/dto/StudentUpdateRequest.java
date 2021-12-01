package com.huhoot.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class StudentUpdateRequest {
    private int id;
    private String fullName;
    private String username;
    private Boolean isNonLocked;
}
