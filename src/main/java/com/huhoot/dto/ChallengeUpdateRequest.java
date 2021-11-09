package com.huhoot.dto;

import com.huhoot.enums.ChallengeStatus;
import lombok.Data;

@Data
public class ChallengeUpdateRequest extends ChallengeAddRequest{
    private int id;
    private boolean isDeleted;
    private ChallengeStatus challengeStatus;

}
