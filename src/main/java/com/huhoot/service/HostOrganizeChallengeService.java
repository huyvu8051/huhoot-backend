package com.huhoot.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.huhoot.dto.*;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
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

    /**
     * @param challengeId {@link Challenge} id
     * @param adminId     {@link Admin} id
     * @param pageable    {@link Pageable}
     * @return List of top 20 student have best total challenge score
     */
    List<StudentScoreResponse> getTopStudent(int challengeId, int adminId, Pageable pageable);

    /**
     * @param adminId    {@link Admin} id
     * @param questionId {@link com.huhoot.model.Question} id
     * @param adminId    {@link Admin} id
     * @return list of answer contain number of student select
     */
    List<AnswerStatisticsResponse> getAnswerStatistics(int adminId, int questionId);

    /**
     * Set challenge status ENDED and sent endChallenge event to all Client in Room
     *
     * @param challengeId {@link Challenge} id
     * @param adminId     {@link Admin} id
     * @throws NotFoundException
     */
    void endChallenge(int challengeId, int adminId) throws NotFoundException;


    /**
     * @param studentIds  List of {@link com.huhoot.model.Student} ids
     * @param challengeId {@link Challenge} id
     * @param adminId     {@link Admin} id
     */
    void kickStudent(List<Integer> studentIds, int challengeId, int adminId);

    PrepareStudentAnswerResponse prepareStudentAnswer(int challengeId, int id);
}
