package com.ats.reservasrestaurante.application.exception;

import com.ats.reservasrestaurante.application.lasting.EMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SecurityException extends Exception{

  private final HttpStatus status;
  private final String message;

  public SecurityException(EMessage eMessage){
    this.status = eMessage.getStatus();
    this.message = eMessage.getMessage();
  }

}
