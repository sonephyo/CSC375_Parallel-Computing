package com.csc375.genetic_algorithm_project1_backend.service;

import com.csc375.genetic_algorithm_project1_backend.facilityLayoutProblemSolution.Layout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class WebSocketService {

    static final int num_of_stations = 48;

    static final int num_of_threads = 32;
    static final int countOfGAOperations = 20000;

    private static final long COOLDOWN_PERIOD = 1000; // 1 minute in milliseconds
    private volatile long lastExecutionTime = 0; // To store the last execution time


    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void generateFLPSolution() throws InterruptedException {
        System.out.println("Starting generating");
        Layout layout = new Layout(num_of_threads, this);
        layout.evaluate(num_of_stations, countOfGAOperations);
    }

    public synchronized void sendData(int[][] generatedData) {
            HashMap<String, int[][]> data = new HashMap<>();
            data.put("data", generatedData);

            // Send the message through the WebSocket
            messagingTemplate.convertAndSend("/topic/reply", data);
    }

}
