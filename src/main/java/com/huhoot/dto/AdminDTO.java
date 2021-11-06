package com.huhoot.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class AdminDTO {
    private Integer id;

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
    private String errorMessage;
    private boolean isNonLocked;
    private Date createdDate;
    private Date modifiedDate;

}
