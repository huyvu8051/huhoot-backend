package com.huhoot.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ChallengeAddRequest {
    @NotBlank
    @NotNull
    @NotEmpty
    private String title;
    private String originalFileName;
    private String base64Image;
    private boolean randomAnswer;
    private boolean randomQuest;

}
