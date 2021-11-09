package com.huhoot.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ChallengeAddRequest {
    @NotBlank
    @NotNull
    @NotEmpty
    private String title;
    private String coverImage;
    private boolean randomAnswer;
    private boolean randomQuest;

}
