package com.huhoot.service.impl;

import com.huhoot.converter.AbstractDtoConverter;
import com.huhoot.converter.ChallengeConverter;
import com.huhoot.dto.*;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.service.HostService;
import com.huhoot.utils.UploadFileUtils;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class HostServiceImpl implements HostService {
    @Autowired
    private ChallengeRepository challengeRepository;

    @Override
    public PageResponse<ChallengeResponse> findAllOwnChallenge(Admin userDetails, Pageable pageable) {
        Page<Challenge> challenges = challengeRepository.findAll(pageable);

        return AbstractDtoConverter.toPageResponse(challenges, ChallengeConverter::toChallengeResponse);
    }

    @Override
    public ChallengeDetails getOneOwnChallengeDetailsById(Admin userDetails, int id, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Challenge challenge = challengeRepository.findOneById(id);
        if (challenge == null) {
            throw new NotFoundException("Challenge not found");
        }
        checker.accept(userDetails, challenge);
        return ChallengeConverter.toChallengeDetails(challenge);
    }

    @Override
    public PageResponse<ChallengeResponse> searchOwnChallengeByTitle(Admin userDetails, String title, Pageable pageable) {
        Page<Challenge> result = challengeRepository.findAllByTitleContainingIgnoreCaseAndAdminId(title, userDetails.getId(), pageable);
        return AbstractDtoConverter.toPageResponse(result, ChallengeConverter::toChallengeResponse);
    }

    @Autowired
    private UploadFileUtils uploadFileUtils;

    @Override
    public void addOneChallenge(Admin userDetails, ChallengeAddRequest request) throws IOException {

        Challenge challenge = ChallengeConverter.toEntity(request);
        String fileName = uploadFileUtils.uploadImage(request.getOriginalFileName(), request.getBase64Image());
        challenge.setAdmin(userDetails);
        challenge.setCoverImage(fileName);
        challengeRepository.save(challenge);
    }



    @Override
    public void updateOneChallenge(Admin userDetails, ChallengeUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException {
        Challenge challenge = challengeRepository.findOneById(request.getId());

        checker.accept(userDetails, challenge);

        challenge.setTitle(request.getTitle());
        //challenge.setCoverImage(request.getCoverImage());
        challenge.setRandomAnswer(request.isRandomAnswer());
        challenge.setRandomQuest(request.isRandomQuest());
        challenge.setChallengeStatus(request.getChallengeStatus());
        challenge.setDeleted(request.isDeleted());

        challengeRepository.save(challenge);

    }

    @Override
    public void deleteManyChallenge(Admin userDetails, List<Integer> ids) {

        /*List<Challenge> challenges = challengeRepository.findAllByAdminIdAndId(userDetails.getId(), ids);

        for(Challenge challenge : challenges){
            challenge.setDeleted(true);
        }

        challengeRepository.saveAll(challenges);*/
    }


}
