package com.huhoot.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.huhoot.exception.ChallengeException;
import com.huhoot.model.Student;
import com.huhoot.student.participate.SendAnswerResponse;
import com.huhoot.student.participate.StudentAnswerRequest;

public interface StudentParticipateService {

    void join(SocketIOClient client, int challengeId, Student student) throws ChallengeException;

    SendAnswerResponse answer(StudentAnswerRequest request, Student userDetails) throws Exception;
}
