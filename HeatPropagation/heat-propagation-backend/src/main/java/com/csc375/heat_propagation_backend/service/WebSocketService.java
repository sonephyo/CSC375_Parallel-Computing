package com.csc375.heat_propagation_backend.service;

import com.csc375.heat_propagation_backend.metalAlloy.MetalAlloy;
import com.csc375.heat_propagation_backend.metalAlloyServerClient.MetalAlloyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;


@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private MetalAlloyClient metalAlloyClient;
    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void startHeatPropagationOperation(HashMap<String, Double> data) throws Exception {

        System.out.println(data.keySet());
        MetalAlloy metalTest = new MetalAlloy(
                data.get("numOfRows").intValue(),
                data.get("topLeftHeat"),
                data.get("bottomRightHeat"),
                 data.get("metal1ThermalConstant"),
                 data.get("metal2ThermalConstant"),
               data.get("metal3ThermalConstant")
        );

        String ip = "129.3.20.1";
//        String ip = "localhost"; // Use when running localhost Server Socket
         metalAlloyClient = new MetalAlloyClient();

        metalAlloyClient.startConnection(ip, 4444, this);

        metalAlloyClient.startHeating(metalTest, data.get("numOfIterations").intValue());
    }

    public synchronized void sendData(double[][] metalAlloyData) {

        HashMap<String, double[][]> hashMapData = new HashMap<>();
        hashMapData.put("metalAlloyData", metalAlloyData);

        messagingTemplate.convertAndSend("/topic/reply", hashMapData);
    }

    public void sendFinish() {

        HashMap<String, Object> data = new HashMap<>();
        data.put("status", "end");
        messagingTemplate.convertAndSend("/topic/status", data);
    }

    public void terminateProgram() throws IOException {
        metalAlloyClient.stopConnection();
    }

}
