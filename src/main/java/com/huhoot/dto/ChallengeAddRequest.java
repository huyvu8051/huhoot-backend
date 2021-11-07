package com.huhoot.dto;

import com.huhoot.model.ChallengeStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ChallengeAddRequest {
    private String title;
    private String coverImage;
    private boolean randomAnswer;
    private boolean randomQuest;
    private ChallengeStatus challengeStatus;

}
