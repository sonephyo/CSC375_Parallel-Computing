package com.csc375.heat_propagation_backend.controllers;


import com.csc375.heat_propagation_backend.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.util.HashMap;

@Controller
@CrossOrigin("http://localhost:5173")
public class WebSocketController {

    private final WebSocketService webSocketService;

    @Autowired
    public WebSocketController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/start")
    public void startScheduler(@Payload HashMap<String, Double> data) throws Exception {
        System.out.println(data);
        webSocketService.startHeatPropagationOperation(data);
    }

    @MessageMapping("/terminate")
    public void stopScheduler() throws IOException {
        webSocketService.terminateProgram();
    }


}
