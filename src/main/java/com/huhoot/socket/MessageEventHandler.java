package com.huhoot.socket;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.huhoot.auth.JwtUtil;
import com.huhoot.auth.MyUserDetailsService;
import com.huhoot.exception.StudentAddException;
import com.huhoot.host.manage.challenge.ChallengeMapper;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.model.Student;
import com.huhoot.organize.OrganizeService;
import com.huhoot.organize.PublishedExam;
import com.huhoot.participate.ParticipateService;
import com.huhoot.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

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
    private final OrganizeService organizeService;

    @OnConnect
    public void onConnect(SocketIOClient client) throws NullPointerException {
        client.sendEvent("connected", "connect success");
        log.info("a client was connected");
    }


    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        Integer challengeId = client.get("challengeId");

        Challenge challenge = challengeRepository.findOneById(challengeId).orElseThrow(() -> new NullPointerException("Challenge not found"));

        if(challenge.isAutoOrganize()){
            organizeService.findAnyClientAndEnableAutoOrganize(challengeId);
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


            client.joinRoom(String.valueOf(request.getChallengeId()));

            PublishedExam currentPublishedExam = participateService.getCurrentPublishedExam(request.getChallengeId());


            client.sendEvent("registerSuccess", HostRegisterSuccess.builder()
                    .totalStudentInChallenge(0)
                    .currentExam(currentPublishedExam)
                    .build());

            client.set("id", admin.getId());
            client.set("challengeId", request.getChallengeId());

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

            Student student = studentRepository.findOneByUsername(username).orElseThrow(() -> new StudentAddException("Student not found"));

            if (!jwtUtil.validateToken(token, student)) {
                throw new Exception("Bad token");
            }
            client.set("id", student.getId());
            client.set("challengeId", request.getChallengeId());
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