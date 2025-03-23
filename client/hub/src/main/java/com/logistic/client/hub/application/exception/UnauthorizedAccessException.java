package com.logistic.client.hub.application.exception;


public class UnauthorizedAccessException extends CustomException {
  public UnauthorizedAccessException(AuthExceptionCode unauthorizedAccess) {
    super(HubExceptionCode.UNAUTHORIZED_ACCESS);
  }
}