package com.huhoot.service.impl;

import com.huhoot.converter.*;
import com.huhoot.dto.*;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.model.*;
import com.huhoot.repository.*;
import com.huhoot.service.HostManageService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HostManageServiceImpl implements HostManageService {
    private final ChallengeRepository challengeRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public HostManageServiceImpl(ChallengeRepository challengeRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, StudentInChallengeRepository studentChallengeRepository, StudentRepository studentRepository, StudentAnswerRepository studentAnswerRepository) {
        this.challengeRepository = challengeRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.studentChallengeRepository = studentChallengeRepository;
        this.studentRepository = studentRepository;
        this.studentAnswerRepository = studentAnswerRepository;
    }

    @Override
    public PageResponse<ChallengeResponse> findAllOwnChallenge(Admin userDetails, Pageable pageable) {
        Page<Challenge> challenges = challengeRepository.findAllByAdminId(userDetails.getId(), pageable);

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
        challenge.setNonDeleted(request.isDeleted());

        challengeRepository.save(challenge);

    }

    @Override
    public void deleteManyChallenge(Admin userDetails, List<Integer> ids) {

        List<Challenge> challenges = challengeRepository.findAllByAdminIdAndIdIn(userDetails.getId(), ids);

        for (Challenge challenge : challenges) {
            challenge.setNonDeleted(false);
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
            question.setNonDeleted(false);
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

        if (question.getAnswers().size() >= 4) {
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
            answer.setNonDeleted(false);
        }

        answerRepository.saveAll(answers);
    }

    private final StudentInChallengeRepository studentChallengeRepository;

    @Override
    public PageResponse<StudentInChallengeResponse> findAllStudentInChallenge(Admin userDetails, Pageable pageable, int challengeId) {
        Page<StudentInChallenge> page = studentChallengeRepository.findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminIdAndIsNonDeletedFalse(challengeId, userDetails.getId(), pageable);

        return ListConverter.toPageResponse(page, StudentInChallengeConverter::toStudentChallengeResponse);
    }

    @Override
    public PageResponse<StudentInChallengeResponse> searchStudentInChallengeByTitle(Admin userDetails, String studentUsername, int challengeId, Pageable pageable) {
        Page<StudentInChallenge> page = studentChallengeRepository.findAllByPrimaryKeyStudentUsernameContainingIgnoreCaseAndPrimaryKeyChallengeId(studentUsername, challengeId, pageable);
        return ListConverter.toPageResponse(page, StudentInChallengeConverter::toStudentChallengeResponse);
    }

    private final StudentRepository studentRepository;

    /**
     *
     */
    @Override
    public List<StudentChallengeAddError> addManyStudentInChallenge(Admin userDetails, StudentInChallengeAddRequest request) throws NotFoundException {
        Optional<Challenge> optional = challengeRepository.findOneById(request.getChallengeId());

        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        List<StudentChallengeAddError> errors = new ArrayList<>();

        for (int id : request.getStudentIds()) {
            try {
                Optional<Student> optionalStudent = studentRepository.findOneById(id);
                Student student = optionalStudent.orElseThrow(() -> new NotFoundException("Student not found"));

                studentChallengeRepository.save(new StudentInChallenge(student, challenge));
            } catch (Exception e) {
                errors.add(new StudentChallengeAddError(id, e.getMessage()));
            }
        }

        return errors;

    }


    @Override
    public void deleteManyStudentInChallenge(Admin userDetails, StudentInChallengeDeleteRequest request) {
        List<StudentInChallenge> list = studentChallengeRepository.findAllByPrimaryKeyStudentIdInAndPrimaryKeyChallengeId(request.getStudentIds(), request.getChallengeId());

        for (StudentInChallenge studentChallenge : list) {
            studentChallenge.setNonDeleted(false);
        }

        studentChallengeRepository.saveAll(list);

    }

    @Override
    public List<StudentInChallengeResponse> openChallenge(Admin userDetails, int challengeId) throws NotFoundException {
        Optional<Challenge> optional = challengeRepository.findOneByIdAndAdminId(challengeId, userDetails.getId());
        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        long t0 = System.nanoTime();
        this.createAllStudentAnswerInChallenge(challenge);
        long t1 = System.nanoTime();
        double elapsedTimeInSecond = (double) (t1 - t0) / 1_000_000_000;
        log.info("Elapsed time =" + elapsedTimeInSecond + " seconds");


        challenge.setChallengeStatus(ChallengeStatus.WAITING);
        challengeRepository.updateChallengeStatusByIdAndAdminId(ChallengeStatus.WAITING, challengeId, userDetails.getId());

        List<StudentInChallenge> studentsInChallenge = studentChallengeRepository.findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminIdAndIsNonDeletedFalse(challengeId, userDetails.getId());
        return ListConverter.toListResponse(studentsInChallenge, StudentInChallengeConverter::toStudentChallengeResponse);

    }


    private final StudentAnswerRepository studentAnswerRepository;

    private void createAllStudentAnswerInChallenge(Challenge challenge) {


        List<Student> students = studentRepository.findAllByStudentChallengesPrimaryKeyChallengeId(challenge.getId());

        List<Question> questions = questionRepository.findAllByChallengeId(challenge.getId());

        List<StudentAnswer> studentAnswers = new ArrayList<>();

        for (Question quest : questions) {
            List<Answer> answers = quest.getAnswers();
            for (Answer ans : answers) {
                for (Student stu : students) {

                    StudentAnswerId id = StudentAnswerId.builder()
                            .student(stu)
                            .answer(ans)
                            .challenge(challenge)
                            .question(quest)
                            .build();
                    
                    studentAnswers.add(StudentAnswer.builder()
                            .primaryKey(id)
                            .score(0)
                            .isCorrect(false)
                            .answerDate(null)
                            .build());

                }
            }
        }


        studentAnswerRepository.saveAll(studentAnswers);

    }


}
