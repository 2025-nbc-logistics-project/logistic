package com.logistic.client.hub.application.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {

  HttpStatus getHttpStatus();

  String getMessage();
}