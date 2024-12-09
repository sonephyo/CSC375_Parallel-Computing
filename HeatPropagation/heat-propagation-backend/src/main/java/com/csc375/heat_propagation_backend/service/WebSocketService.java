package com.csc375.heat_propagation_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void generateFLPSolution(String data) throws InterruptedException {
        HashMap<String, Object> something = new HashMap<>();
        something.put("status", "startTestFromBackend from /topic/status");
        messagingTemplate.convertAndSend("/topic/status", something);


        for (int i = 0; i < 5; i++) {
            Thread.sleep(1000);
            this.sendData();
        }
    }

    public synchronized void sendData() {
            // Send the message through the WebSocket
        HashMap<String, String> data = new HashMap<>();
        data.put("status", "startTestFromBackend");
            messagingTemplate.convertAndSend("/topic/reply", data);
    }

    public void sendFinish() {

        HashMap<String, Object> data = new HashMap<>();
        data.put("status", "end");
        messagingTemplate.convertAndSend("/topic/status", data);
    }

    public void terminateProgram() {
        System.exit(0);
    }

}
