package com.huhoot.service;

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

    void publishQuestion(Admin userDetails, int questionId) throws NotFoundException;

    void showCorrectAnswer(Admin userDetails, int questionId, String challengeId);

    List<StudentScoreResponse> getTopStudent(Admin userDetails, int challengeId, Pageable pageable);

    List<AnswerStatisticsResponse> getAnswerStatistics(int hostId, int questionId);

    void endChallenge(int challengeId, int id) throws NotFoundException;

    void kickStudent(List<Integer> studentIds, int challengeId, int id);

    PrepareStudentAnswerResponse prepareStudentAnswer(int challengeId, int id);
}
