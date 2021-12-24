package com.huhoot.converter;

import com.huhoot.enums.ChallengeStatus;
import com.huhoot.host.manage.challenge.ChallengeAddRequest;
import com.huhoot.host.manage.challenge.ChallengeResponse;
import com.huhoot.model.Challenge;
import org.springframework.stereotype.Component;

@Component
public class ChallengeConverter {

    public static ChallengeResponse toChallengeResponse(Challenge entity) {
        ChallengeResponse response = new ChallengeResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setCoverImage(entity.getCoverImage());
        response.setRandomAnswer(entity.isRandomAnswer());
        response.setRandomQuest(entity.isRandomQuest());
        response.setChallengeStatus(entity.getChallengeStatus());
        response.setOwner(entity.getAdmin().getUsername());
        if(entity.getCreatedDate() != null){
            response.setCreatedDate(entity.getCreatedDate().getTime());
        }
        if(entity.getModifiedDate() != null){
            response.setModifiedDate(entity.getModifiedDate().getTime());
        }
        response.setCreatedBy(entity.getCreatedBy());

        response.setModifiedBy(entity.getModifiedBy());
        return response;
    }

    public static Challenge toEntity(ChallengeAddRequest request) {
        Challenge challenge = new Challenge();
        challenge.setTitle(request.getTitle());
        // challenge.setCoverImage(request.getCoverImage());
        challenge.setRandomAnswer(request.isRandomAnswer());
        challenge.setRandomQuest(request.isRandomQuest());
        challenge.setChallengeStatus(ChallengeStatus.WAITING);
        return challenge;
    }
}
