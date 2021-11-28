package com.huhoot.dto;

import com.huhoot.enums.ChallengeStatus;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
public class ChallengeResponse {
    private int id;
    private String title;
    private String coverImage;
    private boolean randomAnswer;
    private boolean randomQuest;
    private ChallengeStatus ChallengeStatus;
    private String owner;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp modifiedDate;
    private String modifiedBy;
    private UUID adminSocketId;
}
