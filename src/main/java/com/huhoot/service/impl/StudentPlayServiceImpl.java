package com.huhoot.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.huhoot.converter.StudentInChallengeConverter;
import com.huhoot.dto.ChallengeResponse;
import com.huhoot.model.Student;
import com.huhoot.model.StudentInChallenge;
import com.huhoot.repository.StudentInChallengeRepository;
import com.huhoot.service.StudentPlayService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentPlayServiceImpl implements StudentPlayService {

    private final SocketIOServer socketIOServer;

    private final StudentInChallengeRepository studentInChallengeRepository;

    public StudentPlayServiceImpl(SocketIOServer socketIOServer, StudentInChallengeRepository studentInChallengeRepository) {
        this.socketIOServer = socketIOServer;
        this.studentInChallengeRepository = studentInChallengeRepository;
    }

    @Override
    public void join(int challengeId, Student userDetails) throws NotFoundException {

        Optional<StudentInChallenge> optional = studentInChallengeRepository.findOneByPrimaryKeyChallengeIdAndPrimaryKeyStudentId(challengeId, userDetails.getId());

        StudentInChallenge studentInChallenge = optional.get();

        studentInChallenge.setLogin(true);

        studentInChallengeRepository.save(studentInChallenge);


    }
}
