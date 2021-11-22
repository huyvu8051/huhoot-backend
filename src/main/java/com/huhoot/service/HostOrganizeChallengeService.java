package com.huhoot.service;

import com.huhoot.dto.QuestionResponse;
import com.huhoot.dto.StudentInChallengeResponse;
import com.huhoot.model.Admin;
import javassist.NotFoundException;

import java.util.List;

public interface HostOrganizeChallengeService {
    List<StudentInChallengeResponse> getAllStudentInChallengeIsLogin(Admin userDetails, int challengeId);

    List<QuestionResponse> startChallenge(Admin userDetails, int challengeId) throws NotFoundException;

    void publishQuestion(Admin userDetails, int questionId) throws NotFoundException;
}
