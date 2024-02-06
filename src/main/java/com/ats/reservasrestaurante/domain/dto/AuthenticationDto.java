package com.ats.reservasrestaurante.domain.dto;

public record AuthenticationDto(
  String userName,
  String password
) {
}
