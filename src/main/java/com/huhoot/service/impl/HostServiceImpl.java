package com.huhoot.service.impl;

import com.huhoot.converter.ChallengeConverter;
import com.huhoot.dto.*;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.service.HostService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.function.BiPredicate;

@Service
public class HostServiceImpl implements HostService {
    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeConverter challengeConverter;

    @Override
    public PageResponse<ChallengeResponse> findAllOwnChallenge(Admin userDetails, Pageable pageable) {
        Page<Challenge> challenges = challengeRepository.findAll(pageable);

        return challengeConverter.toPageChallengeResponse(challenges);
    }

    @Override
    public ChallengeDetails getOneOwnChallengeDetailsById(Admin userDetails, int id) throws NotYourOwnException, NotFoundException {
        return null;
    }

    @Override
    public PageResponse<ChallengeResponse> searchOwnChallengeByTitle(Admin userDetails, String title, Pageable pageable) {
        return null;
    }

    @Override
    public void addOneChallenge(Admin userDetails, ChallengeAddRequest request) {
        Challenge challenge = challengeConverter.toEntity(request);
        challenge.setAdmin(userDetails);
        challengeRepository.save(challenge);
    }

    @Override
    public void updateOneChallenge(Admin userDetails, ChallengeUpdateRequest request, BiPredicate<Integer, Integer> isNotOwner) throws NotYourOwnException {
        Challenge challenge = challengeRepository.findOneById(request.getId());
        if (isNotOwner.test(challenge.getAdmin().getId(), userDetails.getId())) {
            throw new NotYourOwnException();
        }

        challenge.setTitle(request.getTitle());
        challenge.setCoverImage(request.getCoverImage());
        challenge.setRandomAnswer(request.isRandomAnswer());
        challenge.setRandomQuest(request.isRandomQuest());
        challenge.setChallengeStatus(request.getChallengeStatus());
        challenge.setDeleted(request.isDeleted());

        challengeRepository.save(challenge);

    }
}
