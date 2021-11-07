package com.huhoot.converter;

import com.huhoot.dto.ChallengeAddRequest;
import com.huhoot.dto.ChallengeResponse;
import com.huhoot.dto.ChallengeUpdateRequest;
import com.huhoot.dto.PageResponse;
import com.huhoot.model.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ChallengeConverter {

    public PageResponse<ChallengeResponse> toPageChallengeResponse(Page<Challenge> challenges) {
        PageResponse<ChallengeResponse> response = new PageResponse<>();

        for (Challenge challenge : challenges) {
            response.getList().add(toChallengeResponse(challenge));
        }

        response.setTotalElements(challenges.getTotalElements());
        return response;

    }

    private ChallengeResponse toChallengeResponse(Challenge entity) {
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

    public Challenge toEntity(ChallengeUpdateRequest request) {
        Challenge challenge = new Challenge();
        challenge.setId(request.getId());
        challenge.setTitle(request.getTitle());
        challenge.setCoverImage(request.getCoverImage());
        challenge.setRandomAnswer(request.isRandomAnswer());
        challenge.setRandomQuest(request.isRandomQuest());
        challenge.setChallengeStatus(request.getChallengeStatus());
        challenge.setDeleted(request.isDeleted());


        return challenge;
    }

    public Challenge toEntity(ChallengeAddRequest request) {
        Challenge challenge = new Challenge();
        challenge.setTitle(request.getTitle());
        challenge.setCoverImage(request.getCoverImage());
        challenge.setRandomAnswer(request.isRandomAnswer());
        challenge.setRandomQuest(request.isRandomQuest());
        challenge.setChallengeStatus(request.getChallengeStatus());

        return challenge;
    }
}
