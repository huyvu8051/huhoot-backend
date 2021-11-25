package com.huhoot.listerner;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.huhoot.dto.SocketAuthorizationRequest;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.model.Student;
import com.huhoot.model.StudentInChallenge;
import com.huhoot.repository.AdminRepository;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.repository.StudentInChallengeRepository;
import com.huhoot.repository.StudentRepository;
import com.huhoot.service.impl.MyUserDetailsService;
import com.huhoot.utils.JwtUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class MessageEventHandler {
    private final SocketIOServer server;
    private final ChallengeRepository challengeRepository;
    private final AdminRepository adminRepository;
    private final MyUserDetailsService myUserDetailsService;
    private final StudentRepository studentRepository;
    private final StudentInChallengeRepository studentInChallengeRepository;

    private final JwtUtil jwtUtil;

    public MessageEventHandler(SocketIOServer server, ChallengeRepository challengeRepository, AdminRepository adminRepository, MyUserDetailsService myUserDetailsService, StudentRepository studentRepository, StudentInChallengeRepository studentInChallengeRepository, JwtUtil jwtUtil) {
        this.server = server;
        this.challengeRepository = challengeRepository;
        this.adminRepository = adminRepository;
        this.myUserDetailsService = myUserDetailsService;
        this.studentRepository = studentRepository;
        this.studentInChallengeRepository = studentInChallengeRepository;
        this.jwtUtil = jwtUtil;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) throws NotFoundException {
        client.sendEvent("connected", "connect success");
        log.info("a client was connected");
    }


    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        client.disconnect();
        log.info("a client was disconnected");

    }

    @OnEvent("messageEvent")
    public void onEvent(SocketIOClient client, AckRequest request, String data) {
        log.info("get data = " + data.toString());
        client.sendEvent("message", "chung ta cua hien tai");
    }

    @OnEvent("registerHostSocket")
    public void registerHostSocket(SocketIOClient client, SocketAuthorizationRequest request) throws Exception {
        String token = request.getToken().substring(7);

        String username = jwtUtil.extractUsername(token);

        Admin admin = adminRepository.findOneByUsername(username);

        if (!jwtUtil.validateToken(token, admin)) {
            throw new Exception("Bad token");
        }

        Optional<Challenge> optionalChallenge = challengeRepository.findOneByIdAndAdminId(request.getChallengeId(), admin.getId());
        Challenge challenge = optionalChallenge.orElseThrow(() -> new NotFoundException("Challenge not found"));


        client.joinRoom(challenge.getId() + "");
        challengeRepository.save(challenge);

        admin.setSocketId(client.getSessionId());
        adminRepository.save(admin);

        log.info("save admin success");
    }

    @OnEvent("clientConnectRequest")
    public void clientConnectRequest(SocketIOClient client, SocketAuthorizationRequest request) throws Exception {
        String token = request.getToken().substring(7);

        String username = jwtUtil.extractUsername(token);

        Student student = studentRepository.findOneByUsername(username);

        if (!jwtUtil.validateToken(token, student)) {
            throw new Exception("Bad token");
        }

        Optional<StudentInChallenge> optional = studentInChallengeRepository.findOneByPrimaryKeyChallengeIdAndPrimaryKeyStudentId(request.getChallengeId(), student.getId());

        StudentInChallenge studentInChallenge = optional.orElseThrow(() -> {
            client.sendEvent("joinError", "Challenge not found");
            client.disconnect();
            return new NotFoundException("Challenge not found");
        });

        client.joinRoom(request.getChallengeId() + "");

        student.setSocketId(client.getSessionId());

        studentRepository.save(student);


    }


}