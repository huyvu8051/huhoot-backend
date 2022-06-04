package com.huhoot.dto;

import com.huhoot.auditing.AuditableDto;
import com.huhoot.enums.ChallengeStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ChallengeResponse extends AuditableDto {
    private int id;
    private String title;
    private String coverImage;
    private boolean randomAnswer;
    private boolean randomQuest;
    private ChallengeStatus challengeStatus;
    private String owner;

    private UUID adminSocketId;

    private Integer userAutoOrganizeId;

    private boolean autoOrganize;

    public ChallengeResponse(int id, String title, String coverImage, boolean randomAnswer, boolean randomQuest,
                             ChallengeStatus challengeStatus, String owner, UUID adminSocketId,
                             Long createdDate, String createdBy, Long modifiedDate, String modifiedBy) {
        super(createdDate, createdBy, modifiedDate, modifiedBy);
        this.id = id;
        this.title = title;
        this.coverImage = coverImage;
        this.randomAnswer = randomAnswer;
        this.randomQuest = randomQuest;
        this.challengeStatus = challengeStatus;
        this.owner = owner;
        this.adminSocketId = adminSocketId;
    }

    public ChallengeResponse(int id, String title, String coverImage, boolean randomAnswer, boolean randomQuest,
                             com.huhoot.enums.ChallengeStatus challengeStatus, String owner, UUID adminSocketId) {
        this.id = id;
        this.title = title;
        this.coverImage = coverImage;
        this.randomAnswer = randomAnswer;
        this.randomQuest = randomQuest;
        this.challengeStatus = challengeStatus;
        this.owner = owner;
        this.adminSocketId = adminSocketId;
    }

}
