package com.ats.reservasrestaurante.application.service;

import com.ats.reservasrestaurante.application.exception.SecurityException;
import com.ats.reservasrestaurante.domain.dto.AuthenticationDto;
import com.ats.reservasrestaurante.domain.dto.UserDto;

public interface AuthenticationService {
    public String register(UserDto userDto) throws SecurityException;
    public String login(AuthenticationDto authenticationDto) throws SecurityException;
}
