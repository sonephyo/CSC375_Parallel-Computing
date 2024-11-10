package com.csc375.performance_measurement_backend.controllers.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Tester {

    @GetMapping("/")
    public String sendTest() {
        return "Hello World";
    }


}
