package com.logistic.client.hub.domain.exception;

import com.logistic.client.hub.application.exception.CustomException;
import com.logistic.client.hub.application.exception.HubExceptionCode;

public class HubAlreadyExistsException extends CustomException {
  public HubAlreadyExistsException(HubExceptionCode code) {
    super(code);
  }
}
