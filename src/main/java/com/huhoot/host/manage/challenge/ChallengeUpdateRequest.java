package com.huhoot.host.manage.challenge;

import com.huhoot.enums.ChallengeStatus;
import lombok.Data;

@Data
public class ChallengeUpdateRequest {
    private int id;
    private String title;
    private String coverImage;
    private Boolean randomAnswer;
    private Boolean randomQuest;
    private Boolean isNonDeleted;
    private ChallengeStatus challengeStatus;

}
