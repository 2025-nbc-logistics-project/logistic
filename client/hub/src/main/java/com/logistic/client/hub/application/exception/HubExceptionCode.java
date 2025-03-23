package com.logistic.client.hub.application.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HubExceptionCode implements ExceptionCode {

  HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "Hub Not Found"),
  HUB_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Hub Already Exists"),
  HUB_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "Hub Already Deleted"),

  UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "Unauthorized Access");

  private final HttpStatus httpStatus;
  private final String message;

}