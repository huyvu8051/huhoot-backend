package com.huhoot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class StudentAddRequest {

    private int id;

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
    private Boolean isNonLocked;



}
