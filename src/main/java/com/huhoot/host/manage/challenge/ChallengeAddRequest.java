package com.huhoot.host.manage.challenge;

import com.huhoot.enums.ChallengeStatus;
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

    private String coverImage;

    private boolean randomAnswer;

    private boolean randomQuest;

    private ChallengeStatus challengeStatus;

    private MultipartFile multipartFile;

}
