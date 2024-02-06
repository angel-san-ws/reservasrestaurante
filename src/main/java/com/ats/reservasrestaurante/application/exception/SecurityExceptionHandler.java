package com.ats.reservasrestaurante.application.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class SecurityExceptionHandler {

  @ExceptionHandler(SecurityException.class)
  public ResponseEntity<?> handleDemoSecurityException(SecurityException exception,
                                                       WebRequest request){
    ExceptionResponse response = new ExceptionResponse(exception.getMessage());
    return new ResponseEntity<>(response, exception.getStatus());
  }

}
