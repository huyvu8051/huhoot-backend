package com.huhoot.dto;

import com.huhoot.enums.ChallengeStatus;
import lombok.Data;

@Data
public class ChallengeAddRequest {
    private String title;
    private String coverImage;
    private boolean randomAnswer;
    private boolean randomQuest;
    private ChallengeStatus challengeStatus;

}
