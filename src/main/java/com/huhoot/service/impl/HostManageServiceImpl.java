package com.huhoot.service.impl;

import com.huhoot.converter.*;
import com.huhoot.dto.*;
import com.huhoot.enums.AnswerOption;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.exception.ChallengeException;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.mapper.AnswerMapper;
import com.huhoot.mapper.ChallengeMapper;
import com.huhoot.mapper.QuestionMapper;
import com.huhoot.mapper.StudentInChallengeMapper;
import com.huhoot.model.*;
import com.huhoot.repository.*;
import com.huhoot.service.HostManageService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class HostManageServiceImpl implements HostManageService {
    private final ChallengeRepository challengeRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    private final ListConverter listConverter;

    @Override
    public PageResponse<ChallengeResponse> findAllOwnChallenge(Admin userDetails, Pageable pageable) {
        Page<Challenge> challenges = challengeRepository.findAllByAdminId(userDetails.getId(), pageable);

        return listConverter.toPageResponse(challenges, ChallengeConverter::toChallengeResponse);
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
        return listConverter.toPageResponse(result, ChallengeConverter::toChallengeResponse);
    }


    @Override
    public ChallengeResponse addOneChallenge(Admin userDetails, ChallengeAddRequest request) {

        Challenge challenge = challengeMapper.toEntity(request);
        challenge.setAdmin(userDetails);

        Challenge saved = challengeRepository.save(challenge);

        return challengeMapper.toDto(saved);

    }

    private final ChallengeMapper challengeMapper;

    @Override
    public void updateOneChallenge(Admin userDetails, ChallengeUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Optional<Challenge> optional = challengeRepository.findOneById(request.getId());
        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));
        checker.accept(userDetails, challenge);
        challengeMapper.update(request, challenge);
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
        return listConverter.toPageResponse(questions, QuestionConverter::toQuestionResponse);
    }

    @Override
    public QuestionResponse addOneQuestion(Admin userDetails, QuestionAddRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Optional<Challenge> optional = challengeRepository.findOneById(request.getChallengeId());

        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        checker.accept(userDetails, challenge);

        Question question = QuestionConverter.toEntity(request);

        question.setChallenge(challenge);

        Question save = questionRepository.save(question);

        return questionMapper.toDto(save);

    }

    @Override
    public QuestionResponse getOneOwnQuestionDetailsById(Admin userDetails, int id, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Optional<Question> optional = questionRepository.findOneById(id);

        Question question = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        checker.accept(userDetails, question.getChallenge());

        return QuestionConverter.toQuestionResponse(question);

    }

    private final QuestionMapper questionMapper;

    @Override
    public void updateOneQuestion(Admin userDetails, QuestionUpdateRequest request, CheckedFunction<Admin, Challenge> checker) throws NotYourOwnException, NotFoundException {
        Optional<Question> optional = questionRepository.findOneById(request.getId());

        Question question = optional.orElseThrow(() -> new NotFoundException("Question not found"));

        checker.accept(userDetails, question.getChallenge());

        questionMapper.update(request, question);

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
    public PageResponse<PublishAnswer> findAllAnswerByQuestionId(Admin userDetails, int questionId, Pageable pageable) {
        Page<Answer> answers = answerRepository.findAllByQuestionChallengeAdminIdAndQuestionId(userDetails.getId(), questionId, pageable);

        return listConverter.toPageResponse(answers, AnswerConverter::toPublishAnswerResponse);
    }

    @Override
    public PublishAnswer getOneAnswerDetailsById(Admin userDetails, int answerId) throws NotFoundException {
        Optional<Answer> optional = answerRepository.findOneByIdAndQuestionChallengeAdminId(answerId, userDetails.getId());

        Answer answer = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        return AnswerConverter.toAnswerResponse(answer);
    }

    @Override
    public void addOneAnswer(Admin userDetails, AnswerAddRequest request) throws Exception {
        Optional<Question> optional = questionRepository.findOneById(request.getQuestionId());

        Question question = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));


        Answer answer = AnswerConverter.toEntity(request);

        answer.setQuestion(question);

        answerRepository.save(answer);

    }

    private final AnswerMapper answerMapper;

    @Override
    public void updateOneAnswer(Admin userDetails, AnswerUpdateRequest request) throws NotFoundException {
        Optional<Answer> optional = answerRepository.findOneById(request.getId());

        Answer answer = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        answerMapper.updateAnswer(request, answer);

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

    private final StudentInChallengeMapper studentInChallengeMapper;

    @Override
    public PageResponse<StudentInChallengeResponse> findAllStudentInChallenge(Admin userDetails, Pageable pageable, int challengeId) {
        Page<StudentInChallenge> page = studentChallengeRepository.findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminId(challengeId, userDetails.getId(), pageable);
        return listConverter.toPageResponse(page, e -> studentInChallengeMapper.toDto(e));
    }

    @Override
    public PageResponse<StudentInChallengeResponse> searchStudentInChallengeByTitle(Admin userDetails, String studentUsername, int challengeId, Pageable pageable) {
        Page<StudentInChallenge> page = studentChallengeRepository.findAllByPrimaryKeyStudentUsernameContainingIgnoreCaseAndPrimaryKeyChallengeId(studentUsername, challengeId, pageable);
        return listConverter.toPageResponse(page, StudentInChallengeConverter::toStudentChallengeResponse);
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
    public void updateStudentInChallenge(Admin userDetails, StudentInChallengeUpdateRequest request) throws NotFoundException {

        Optional<StudentInChallenge> optional = studentChallengeRepository.findOneByPrimaryKeyStudentIdAndPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminId(request.getStudentId(), request.getChallengeId(), userDetails.getId());

        StudentInChallenge studentInChallenge = optional.orElseThrow(() -> new NotFoundException("Student in challenge not found"));

        studentInChallengeMapper.update(request, studentInChallenge);

        studentChallengeRepository.save(studentInChallenge);

    }

    @Override
    public List<StudentInChallengeResponse> openChallenge(Admin userDetails, int challengeId) throws Exception {
        Optional<Challenge> optional = challengeRepository.findOneByIdAndAdminId(challengeId, userDetails.getId());
        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        long t0 = System.nanoTime();
        this.createAllStudentAnswerInChallenge(challenge);
        long t1 = System.nanoTime();
        double elapsedTimeInSecond = (double) (t1 - t0) / 1_000_000_000;
        log.info("Elapsed time =" + elapsedTimeInSecond + " seconds");


        challenge.setChallengeStatus(ChallengeStatus.WAITING);
        challengeRepository.updateChallengeStatusByIdAndAdminId(ChallengeStatus.WAITING, challengeId, userDetails.getId());

        List<StudentInChallenge> studentsInChallenge = studentChallengeRepository.findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminId(challengeId, userDetails.getId());
        return listConverter.toListResponse(studentsInChallenge, StudentInChallengeConverter::toStudentChallengeResponse);

    }

    private final StudentAnswerRepository studentAnswerRepository;

    private void createAllStudentAnswerInChallenge(Challenge challenge) throws Exception {


        List<Student> students = studentRepository.findAllStudentInChallenge(challenge.getId());

        List<Question> questions = questionRepository.findAllByChallengeId(challenge.getId());

        if(students.size() == 0 || questions.size() == 0) throw new ChallengeException("No student or question found in challenge id = " + challenge.getId());

        List<StudentAnswer> studentAnswers = new ArrayList<>();

        for (Question quest : questions) {
            List<Answer> answers = quest.getAnswers();

            validateQuestion(quest, answers);

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

    private void validateQuestion(Question quest, List<Answer> answers) throws ChallengeException {
        if(answers.size() == 0) throw new ChallengeException("No answer found for question id = " + quest.getId());

        boolean noAnswerCorrect = answers.stream().noneMatch(e -> e.isCorrect());

        if(noAnswerCorrect){
            throw new ChallengeException("No any answer correct for question id = " + quest.getId());
        }

        if(quest.getAnswerOption().equals(AnswerOption.SINGLE_SELECT)){
            long answerCount = answers.stream().filter(e -> e.isCorrect()).count();
            if(answerCount > 1) throw new ChallengeException("SINGLE_SELECT: Ony one answer is correct for question id = " + quest.getId());

        }
    }


}
