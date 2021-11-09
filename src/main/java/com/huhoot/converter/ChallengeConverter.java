package com.huhoot.converter;

import com.huhoot.dto.*;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.model.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ChallengeConverter {

    public static ChallengeDetails toChallengeDetails(Challenge entity) {
        ChallengeDetails response = new ChallengeDetails();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setCoverImage(entity.getCoverImage());
        response.setRandomAnswer(entity.isRandomAnswer());
        response.setRandomQuest(entity.isRandomQuest());
        response.setChallengeStatus(entity.getChallengeStatus());
        response.setOwner(entity.getAdmin().getUsername());
        response.setCreatedDate(entity.getCreatedDate());
        response.setCreatedBy(entity.getCreatedBy());
        response.setModifiedDate(entity.getModifiedDate());
        response.setModifiedBy(entity.getModifiedBy());
        return response;
    }

    public static ChallengeResponse toChallengeResponse(Challenge entity) {
        ChallengeResponse response = new ChallengeResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setCoverImage(entity.getCoverImage());
        response.setRandomAnswer(entity.isRandomAnswer());
        response.setRandomQuest(entity.isRandomQuest());
        response.setChallengeStatus(entity.getChallengeStatus());
        response.setOwner(entity.getAdmin().getUsername());
        response.setCreatedDate(entity.getCreatedDate());
        response.setCreatedBy(entity.getCreatedBy());
        response.setModifiedDate(entity.getModifiedDate());
        response.setModifiedBy(entity.getModifiedBy());
        return response;
    }

    public static Challenge toEntity(ChallengeAddRequest request) {
        Challenge challenge = new Challenge();
        challenge.setTitle(request.getTitle());
        challenge.setCoverImage(request.getCoverImage());
        challenge.setRandomAnswer(request.isRandomAnswer());
        challenge.setRandomQuest(request.isRandomQuest());
        challenge.setChallengeStatus(ChallengeStatus.WAITING);
        return challenge;
    }
}
