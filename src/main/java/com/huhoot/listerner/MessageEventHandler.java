package com.huhoot.listerner;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageEventHandler {
    private final SocketIOServer server;

    public MessageEventHandler(SocketIOServer server) {
        this.server = server;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {

        String s1 = client.getSessionId().toString();



        String gameId = client.getHandshakeData().getSingleUrlParam("gameId");
        client.joinRoom(gameId);
        log.info("connect to GameId = " + gameId);
        client.sendEvent("connect", "connect success");
        server.getRoomOperations(gameId).sendEvent("message", "Nguoi theo huong hoa may mu giang loi");
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

    @OnEvent("registHostSocket")
    public void registHostSocket(SocketIOClient client, AckRequest request, String data) {


        String authorization = client.getHandshakeData().getHttpHeaders().get("Authorization");



        log.info("get data = " + data.toString());
        client.sendEvent("message", "chung ta cua hien tai");
    }
}