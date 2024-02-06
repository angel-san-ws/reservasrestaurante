package com.ats.reservasrestaurante.application.controller;

import com.ats.reservasrestaurante.application.exception.SecurityException;
import com.ats.reservasrestaurante.application.exception.MessageResponse;
import com.ats.reservasrestaurante.domain.dto.AuthenticationDto;
import com.ats.reservasrestaurante.domain.dto.UserDto;
import com.ats.reservasrestaurante.application.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public record AuthenticationController(
        AuthenticationService authenticationService
) {

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto){
        try{
            String token = authenticationService.register(userDto);
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        }catch(SecurityException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDto authenticationDto) throws SecurityException {
        try{
            System.out.println("Login: "+authenticationDto);
            String token = authenticationService.login(authenticationDto);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }catch(SecurityException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }

    }

}