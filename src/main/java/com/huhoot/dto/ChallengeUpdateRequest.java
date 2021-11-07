package com.huhoot.dto;

import lombok.Data;

@Data
public class ChallengeUpdateRequest extends ChallengeAddRequest{
    private int id;
    private boolean isDeleted;

}
