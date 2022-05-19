package com.huhoot.socket;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.huhoot.auth.JwtUtil;
import com.huhoot.auth.MyUserDetailsService;
import com.huhoot.exception.StudentAddException;
import com.huhoot.host.manage.challenge.ChallengeMapper;
import com.huhoot.host.manage.challenge.ChallengeResponse;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.model.Student;
import com.huhoot.participate.ParticipateService;
import com.huhoot.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
public class MessageEventHandler {
    private final SocketIOServer server;
    private final ChallengeRepository challengeRepository;
    private final AdminRepository adminRepository;
    private final MyUserDetailsService myUserDetailsService;
    private final StudentRepository studentRepository;
    private final StudentInChallengeRepository studentInChallengeRepository;

    private final JwtUtil jwtUtil;

    private final QuestionRepository questionRepository;

    private final ChallengeMapper challengeMapper;

    @OnConnect
    public void onConnect(SocketIOClient client) throws NullPointerException {
        client.sendEvent("connected", "connect success");
        log.info("a client was connected");
    }


    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {

        Integer adminId = client.get("adminId");
        if(adminId != null){

        }

        client.disconnect();
        log.info("a client was disconnected");

    }

    @OnEvent("messageEvent")
    public void onEvent(SocketIOClient client, AckRequest request, String data) {

    }

    @OnEvent("registerHostSocket")
    public void registerHostSocket(SocketIOClient client, SocketAuthorizationRequest request) throws Exception {
        try {
            String token = request.getToken().substring(7);
            String username = jwtUtil.extractUsername(token);
            Admin admin = adminRepository.findOneByUsername(username).orElseThrow(() -> new NullPointerException("Admin not found"));

            if (!jwtUtil.validateToken(token, admin)) {
                throw new Exception("Bad token");
            }

            // missing set security context holder

            Challenge challenge = challengeRepository.findOneByIdAndAdminId(request.getChallengeId(), admin.getId()).orElseThrow(() -> new NullPointerException("Challenge not found"));
            client.joinRoom(String.valueOf(challenge.getId()));

            ChallengeResponse challengeResponse = challengeMapper.toDto(challenge);
            client.sendEvent("registerSuccess", challengeResponse);

            client.set("adminId", admin.getId());

            admin.setSocketId(client.getSessionId());
            adminRepository.save(admin);

        } catch (Exception e) {
            log.error(e.getMessage());
            client.sendEvent("joinError", "joinError");
            client.disconnect();
        }
    }

    private final ParticipateService participateService;

    @OnEvent("clientConnectRequest")
    public void clientConnectRequest(SocketIOClient client, SocketAuthorizationRequest request) throws Exception {
        try {

            // authorization
            String token = request.getToken().substring(7);

            String username = jwtUtil.extractUsername(token);

            Optional<Student> optionalStudent = studentRepository.findOneByUsername(username);

            Student student = optionalStudent.orElseThrow(() -> new StudentAddException("Student not found"));

            if (!jwtUtil.validateToken(token, student)) {
                throw new Exception("Bad token");
            }

            // missing set security context holder

            participateService.join(client, request.getChallengeId(), student);

            log.info("Client connect socket success!");
        } catch (Exception e) {
            log.error(e.getMessage());
            client.sendEvent("joinError", "joinError");
            client.disconnect();
        }

    }


}