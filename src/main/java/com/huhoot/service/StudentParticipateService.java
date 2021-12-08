package com.huhoot.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.huhoot.dto.SendAnswerResponse;
import com.huhoot.dto.StudentAnswerRequest;
import com.huhoot.exception.ChallengeException;
import com.huhoot.model.Student;
import io.netty.channel.ChannelException;
import javassist.NotFoundException;

public interface StudentParticipateService {

    void join(SocketIOClient client, int challengeId, Student student) throws ChallengeException;

    SendAnswerResponse answer(StudentAnswerRequest request, Student userDetails) throws Exception;
}
