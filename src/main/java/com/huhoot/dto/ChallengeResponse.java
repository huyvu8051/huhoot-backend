package com.huhoot.dto;

import com.huhoot.enums.ChallengeStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ChallengeResponse {
    private int id;
    private String title;
    private String coverImage;
    private boolean randomAnswer;
    private boolean randomQuest;
    private ChallengeStatus ChallengeStatus;
    private String owner;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;
}
