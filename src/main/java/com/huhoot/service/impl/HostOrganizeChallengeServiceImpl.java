package com.huhoot.service.impl;

import com.corundumstudio.socketio.SocketIOServer;
import com.huhoot.converter.ListConverter;
import com.huhoot.converter.QuestionConverter;
import com.huhoot.converter.StudentInChallengeConverter;
import com.huhoot.dto.PublishQuestionResponse;
import com.huhoot.dto.QuestionResponse;
import com.huhoot.dto.StudentInChallengeResponse;
import com.huhoot.enums.ChallengeStatus;
import com.huhoot.model.*;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.repository.QuestionRepository;
import com.huhoot.repository.StudentInChallengeRepository;
import com.huhoot.service.HostOrganizeChallengeService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HostOrganizeChallengeServiceImpl implements HostOrganizeChallengeService {

    private final QuestionRepository questionRepository;
    private final StudentInChallengeRepository studentInChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final SocketIOServer socketIOServer;


    public HostOrganizeChallengeServiceImpl(QuestionRepository questionRepository, StudentInChallengeRepository studentInChallengeRepository, ChallengeRepository challengeRepository, SocketIOServer socketIOServer) {
        this.questionRepository = questionRepository;
        this.studentInChallengeRepository = studentInChallengeRepository;
        this.challengeRepository = challengeRepository;
        this.socketIOServer = socketIOServer;
    }

    @Override
    public List<StudentInChallengeResponse> getAllStudentInChallengeIsLogin(Admin userDetails, int challengeId) {
        List<StudentInChallenge> studentInChallenges = studentInChallengeRepository.findAllByPrimaryKeyChallengeIdAndPrimaryKeyChallengeAdminIdAndIsLoginTrue(challengeId, userDetails.getId());

        return ListConverter.toListResponse(studentInChallenges, StudentInChallengeConverter::toStudentChallengeResponse);

    }

    @Override
    public List<QuestionResponse> startChallenge(Admin userDetails, int challengeId) throws NotFoundException {

        Optional<Challenge> optional = challengeRepository.findOneByIdAndAdminId(challengeId, userDetails.getId());
        Challenge challenge = optional.orElseThrow(() -> new NotFoundException("Challenge not found"));

        challenge.setChallengeStatus(ChallengeStatus.IN_PROGRESS);
        challengeRepository.save(challenge);

        List<Question> questions = questionRepository.findAllByChallengeIdAndChallengeAdminId(challengeId, userDetails.getId());
        return ListConverter.toListResponse(questions, QuestionConverter::toQuestionResponse);
    }

    @Override
    public void publishQuestion(Admin userDetails, int questionId) throws NotFoundException {
        Optional<Question> optional = questionRepository.findOneByIdAndChallengeAdminId(questionId, userDetails.getId());
        Question question = optional.orElseThrow(() -> new NotFoundException("Question not found"));
        int challengeId = question.getChallenge().getId();
        question.setAskDate(new Date());

        PublishQuestionResponse publishQuestionResponse = QuestionConverter.toPublishQuestionResponse(question);

        socketIOServer.getRoomOperations(challengeId + "").sendEvent("publishQuestion", publishQuestionResponse);
    }
}
