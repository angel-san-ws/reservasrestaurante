package com.ats.reservasrestaurante.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class PublicTestController {
    @GetMapping
    public String allAccess() {
        return "Public Content.";
    }
}
