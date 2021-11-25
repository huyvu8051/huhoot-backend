package com.huhoot.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.huhoot.dto.*;
import com.huhoot.model.Admin;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HostOrganizeChallengeService {
    List<StudentInChallengeResponse> getAllStudentInChallengeIsLogin(Admin userDetails, int challengeId);

    /**
     * Start challenge, update challenge status to IN_PROGRESS
     *
     * @param challengeId challenge id
     * @param adminId     admin id
     * @return List of QuestionResponse
     */
    List<QuestionResponse> startChallenge(int challengeId, int adminId);

    /**
     * Publish a question and answers to all clients in Room.
     * Set publish time to question and update at db
     *
     * @param questionId question id
     * @param adminId    admin id
     * @throws NotFoundException challenge not found
     */
    void publishQuestion(int questionId, int adminId) throws NotFoundException;

    /**
     * Sent all {@link AnswerResponse} to all {@link SocketIOClient} in {@link com.corundumstudio.socketio.BroadcastOperations}
     *
     * @param questionId {@link com.huhoot.model.Question} id
     * @param adminId    {@link Admin} id
     * @throws NotFoundException not found exception
     */
    void showCorrectAnswer(int questionId, int adminId) throws NotFoundException;

    List<StudentScoreResponse> getTopStudent(int challengeId, int adminId, Pageable pageable);

    List<AnswerStatisticsResponse> getAnswerStatistics(int hostId, int questionId);

    void endChallenge(int challengeId, int id) throws NotFoundException;

    void kickStudent(List<Integer> studentIds, int challengeId, int id);

    PrepareStudentAnswerResponse prepareStudentAnswer(int challengeId, int id);
}
