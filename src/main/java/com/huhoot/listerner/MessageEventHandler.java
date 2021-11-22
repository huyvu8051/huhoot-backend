package com.huhoot.listerner;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.huhoot.dto.RegisterHostRequest;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import com.huhoot.repository.AdminRepository;
import com.huhoot.repository.ChallengeRepository;
import com.huhoot.service.impl.MyUserDetailsService;
import com.huhoot.utils.JwtUtil;
import com.sun.deploy.xml.BadTokenException;
import javassist.NotFoundException;
import javassist.tools.web.BadHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class MessageEventHandler {
    private final SocketIOServer server;
    private final ChallengeRepository challengeRepository;
    private final AdminRepository adminRepository;
    private final MyUserDetailsService myUserDetailsService;

    private final JwtUtil jwtUtil;

    public MessageEventHandler(SocketIOServer server, ChallengeRepository challengeRepository, AdminRepository adminRepository, MyUserDetailsService myUserDetailsService, JwtUtil jwtUtil) {
        this.server = server;
        this.challengeRepository = challengeRepository;
        this.adminRepository = adminRepository;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) throws NotFoundException {

        String challengeId = client.getHandshakeData().getSingleUrlParam("challengeId");

        client.joinRoom(challengeId);

        log.info("connect to GameId = " + challengeId);
        client.sendEvent("connected", "connect success");
        // server.getRoomOperations(challengeId).sendEvent("message", "Nguoi theo huong hoa may mu giang loi");
    }


    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String gameId = client.getHandshakeData().getSingleUrlParam("gameId");
        client.disconnect();
        log.info("disconnect to GameId = " + gameId);

    }

    @OnEvent("messageEvent")
    public void onEvent(SocketIOClient client, AckRequest request, String data) {
        log.info("get data = " + data.toString());
        client.sendEvent("message", "chung ta cua hien tai");
    }

    @OnEvent("registerHostSocket")
    public void registerHostSocket(SocketIOClient client, RegisterHostRequest registerRequest) throws Exception {
        String token = registerRequest.getToken().substring(7);

        String username = jwtUtil.extractUsername(token);

        Admin userDetails = adminRepository.findOneByUsername(username);

        if(!jwtUtil.validateToken(token, userDetails)){
            throw new Exception("Bad token");
        }

        Optional<Challenge> optionalChallenge = challengeRepository.findOneByIdAndAdminId(registerRequest.getChallengeId(), userDetails.getId());
        Challenge challenge = optionalChallenge.orElseThrow(() -> new NotFoundException("Challenge not found"));

        challenge.setAdminSocketId(client.getSessionId());
        challengeRepository.save(challenge);

    }
}