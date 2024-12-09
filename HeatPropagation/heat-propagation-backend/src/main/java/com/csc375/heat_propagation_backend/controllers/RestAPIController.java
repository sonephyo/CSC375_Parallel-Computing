package com.csc375.heat_propagation_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin("http://localhost:5173")
public class RestAPIController {

    @GetMapping("/")
    public ResponseEntity<String> testing() {
        return ResponseEntity.ok("Hello World!");
    }


}
