package com.huhoot.service.impl;

import com.huhoot.converter.AbstractDtoConverter;
import com.huhoot.converter.ChallengeConverter;
import com.huhoot.converter.QuestionConverter;
import com.huhoot.dto.*;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.model.Question;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.repository.QuestionRepository;
import com.huhoot.service.HostService;
import javassist.NotFoundException;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Service
public class HostServiceImpl implements HostService {
    @Autowired
    private ChallengeRepository challengeRepository;

    @Override
    public PageResponse<ChallengeResponse> findAllOwnChallenge(Admin userDetails, Pageable pageable) {
        Page<Challenge> challenges = challengeRepository.findAllByAdminIdAndIsDeletedFalse(userDetails.getId(), pageable);

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


    @Override
    public void addOneChallenge(Admin userDetails, ChallengeAddRequest request) throws IOException {

        Challenge challenge = ChallengeConverter.toEntity(request);
        challenge.setAdmin(userDetails);
        challenge.setCoverImage(request.getOriginalFileName());
        challengeRepository.save(challenge);
    }


    @Override
    public void updateOneChallenge(Admin userDetails, ChallengeUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException {
        Challenge challenge = challengeRepository.findOneById(request.getId());

        checker.accept(userDetails, challenge);

        challenge.setTitle(request.getTitle());

        challenge.setCoverImage(request.getOriginalFileName());
        challenge.setRandomAnswer(request.isRandomAnswer());
        challenge.setRandomQuest(request.isRandomQuest());
        challenge.setChallengeStatus(request.getChallengeStatus());
        challenge.setDeleted(request.isDeleted());

        challengeRepository.save(challenge);

    }

    @Override
    public void deleteManyChallenge(Admin userDetails, List<Integer> ids) {

        List<Challenge> challenges = challengeRepository.findAllByAdminIdAndIdInAndIsDeletedFalse(userDetails.getId(), ids);

        for (Challenge challenge : challenges) {
            challenge.setDeleted(true);
        }

        challengeRepository.saveAll(challenges);
    }

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public PageResponse<QuestionResponse> findAllQuestionInChallenge(Admin userDetails, int challengeId, Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByChallengeIdAndChallengeAdminId(challengeId, userDetails.getId(), pageable);
        return AbstractDtoConverter.toPageResponse(questions, QuestionConverter::toQuestionResponse);
    }

    @Override
    public void addOneQuestion(Admin userDetails, QuestionAddRequest request, CheckedFunction<Admin, Challenge> checker) throws NotFoundException, NotYourOwnException {
        Challenge challenge = challengeRepository.findOneById(request.getChallengeId());
        if (challenge == null) {
            throw new NotFoundException("Challenge not found");
        }

        checker.accept(userDetails, challenge);

        Question question = QuestionConverter.toEntity(request);

        questionRepository.save(question);

    }

    @Override
    public QuestionDetails getOneOwnQuestionDetailsById(Admin userDetails, int id, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException {
        Question question = questionRepository.findOneById(id);

        checker.accept(userDetails, question.getChallenge());

        return QuestionConverter.toQuestionDetails(question);

    }


    @Override
    public void updateOneQuestion(Admin userDetails, QuestionUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Question question = questionRepository.findOneById(request.getId());

        if (question == null) {
            throw new NotFoundException("Question not found");
        }

        checker.accept(userDetails, question.getChallenge());

        question.setOrdinalNumber(request.getOrdinalNumber());
        question.setQuestionContent(request.getQuestionContent());
        question.setAnswerTimeLimit(request.getAnswerTimeLimit());
        question.setPoint(request.getPoint());
        question.setAnswerOption(request.getAnswerOption());

        questionRepository.save(question);

    }

    @Override
    public void deleteManyQuestion(Admin userDetails, List<Integer> ids) {
        List<Question> questions = questionRepository.findAllByIdIn(ids);

        for (Question question : questions){
            question.setDeleted(true);
        }

        questionRepository.saveAll(questions);
    }


}
