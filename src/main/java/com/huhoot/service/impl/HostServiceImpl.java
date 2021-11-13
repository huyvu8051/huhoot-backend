package com.huhoot.service.impl;

import com.huhoot.converter.AnswerConverter;
import com.huhoot.converter.ChallengeConverter;
import com.huhoot.converter.ListConverter;
import com.huhoot.converter.QuestionConverter;
import com.huhoot.dto.*;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.model.Admin;
import com.huhoot.model.Answer;
import com.huhoot.model.Challenge;
import com.huhoot.model.Question;
import com.huhoot.repository.AnswerRepository;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.repository.QuestionRepository;
import com.huhoot.service.HostService;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HostServiceImpl implements HostService {
    private final ChallengeRepository challengeRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public HostServiceImpl(ChallengeRepository challengeRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.challengeRepository = challengeRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public PageResponse<ChallengeResponse> findAllOwnChallenge(Admin userDetails, Pageable pageable) {
        Page<Challenge> challenges = challengeRepository.findAllByAdminIdAndIsDeletedFalse(userDetails.getId(), pageable);

        return ListConverter.toPageResponse(challenges, ChallengeConverter::toChallengeResponse);
    }

    @Override
    public ChallengeResponse getOneOwnChallengeDetailsById(Admin userDetails, int id, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Optional<Challenge> optional = challengeRepository.findOneById(id);

        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        checker.accept(userDetails, challenge);

        return ChallengeConverter.toChallengeResponse(challenge);
    }

    @Override
    public PageResponse<ChallengeResponse> searchOwnChallengeByTitle(Admin userDetails, String title, Pageable pageable) {
        Page<Challenge> result = challengeRepository.findAllByTitleContainingIgnoreCaseAndAdminId(title, userDetails.getId(), pageable);
        return ListConverter.toPageResponse(result, ChallengeConverter::toChallengeResponse);
    }


    @Override
    public void addOneChallenge(Admin userDetails, ChallengeAddRequest request) {

        Challenge challenge = ChallengeConverter.toEntity(request);
        challenge.setAdmin(userDetails);
        challenge.setCoverImage(request.getOriginalFileName());
        challengeRepository.save(challenge);
    }


    @Override
    public void updateOneChallenge(Admin userDetails, ChallengeUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Optional<Challenge> optional = challengeRepository.findOneById(request.getId());

        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

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

        List<Challenge> challenges = challengeRepository.findAllByAdminIdAndIdIn(userDetails.getId(), ids);

        for (Challenge challenge : challenges) {
            challenge.setDeleted(true);
        }

        challengeRepository.saveAll(challenges);
    }


    @Override
    public PageResponse<QuestionResponse> findAllQuestionInChallenge(Admin userDetails, int challengeId, Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByChallengeIdAndChallengeAdminId(challengeId, userDetails.getId(), pageable);
        return ListConverter.toPageResponse(questions, QuestionConverter::toQuestionResponse);
    }

    @Override
    public void addOneQuestion(Admin userDetails, QuestionAddRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Optional<Challenge> optional = challengeRepository.findOneById(request.getChallengeId());

        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        checker.accept(userDetails, challenge);

        Question question = QuestionConverter.toEntity(request);

        question.setChallenge(challenge);

        questionRepository.save(question);

    }

    @Override
    public QuestionResponse getOneOwnQuestionDetailsById(Admin userDetails, int id, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Optional<Question> optional = questionRepository.findOneById(id);

        Question question = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        checker.accept(userDetails, question.getChallenge());

        return QuestionConverter.toQuestionResponse(question);

    }


    @Override
    public void updateOneQuestion(Admin userDetails, QuestionUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Optional<Question> optional = questionRepository.findOneById(request.getId());

        Question question = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

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

        for (Question question : questions) {
            question.setDeleted(true);
        }

        questionRepository.saveAll(questions);
    }


    @Override
    public List<AnswerResponse> findAllAnswerByQuestionId(Admin userDetails, int questionId) {
        List<Answer> answers = answerRepository.findAllByQuestionChallengeAdminIdAndQuestionId(userDetails.getId(), questionId);
        return ListConverter.toListResponse(answers, AnswerConverter::toAnswerResponse);
    }

    @Override
    public AnswerResponse getOneAnswerDetailsById(Admin userDetails, int answerId) throws NotFoundException {
        Optional<Answer> optional = answerRepository.findOneByIdAndQuestionChallengeAdminId(answerId, userDetails.getId());

        Answer answer = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        return AnswerConverter.toAnswerResponse(answer);
    }

    @Override
    public void addOneAnswer(Admin userDetails, AnswerAddRequest request) throws Exception {
        Optional<Question> optional = questionRepository.findOneById(request.getQuestionId());

        Question question = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        if(question.getAnswers().size() >=4 ){
            throw new Exception("Reach maximum answer");
        }

        Answer answer = AnswerConverter.toEntity(request);

        answer.setQuestion(question);

        answerRepository.save(answer);

    }

    @Override
    public void updateOneAnswer(Admin userDetails, AnswerUpdateRequest request) throws NotFoundException {
        Optional<Answer> optional = answerRepository.findOneById(request.getId());

        Answer answer = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        answer.setOrdinalNumber(request.getOrdinalNumber());
        answer.setAnswerContent(request.getAnswerContent());
        answer.setCorrect(request.isCorrect());

        answerRepository.save(answer);
    }

    @Override
    public void deleteManyAnswer(Admin userDetails, List<Integer> ids) {
        List<Answer> answers = answerRepository.findAllByIdIn(ids);
        for (Answer answer : answers) {
            answer.setDeleted(true);
        }

        answerRepository.saveAll(answers);
    }

    @Override
    public PageResponse<StudentInChallengeResponse> findAllStudentInChallenge(Admin userDetails, Pageable pageable, int challengeId) {
        return null;
    }

    @Override
    public PageResponse<StudentInChallengeResponse> searchStudentInChallengeByTitle(Admin userDetails, int studentUsername, Pageable pageable) {
        return null;
    }

    @Override
    public void addOneStudentInChallenge(Admin userDetails, StudentInChallengeAddRequest request) {

    }

    @Override
    public void deleteManyStudentInChallenge(Admin userDetails, List<Integer> ids) {

    }


}
