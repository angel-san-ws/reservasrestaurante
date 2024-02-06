package com.ats.reservasrestaurante.domain.dto;

import com.ats.reservasrestaurante.application.lasting.ERole;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        Object id,
        String userName,
        String password,
        String email,
        String phone,
        String firstName,
        String lastName,
        Boolean enable,
        ERole role
) {
}