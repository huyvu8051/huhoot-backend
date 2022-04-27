package com.huhoot.student.participate;

import com.corundumstudio.socketio.SocketIOClient;
import com.huhoot.exception.ChallengeException;
import com.huhoot.model.Student;

public interface StudentParticipateService {

    void join(SocketIOClient client, int challengeId, Student student) throws ChallengeException;

    SendAnswerResponse sendAnswer(StudentAnswerRequest request, Student userDetails) throws Exception;
}
