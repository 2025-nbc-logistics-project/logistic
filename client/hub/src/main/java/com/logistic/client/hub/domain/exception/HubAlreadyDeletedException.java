package com.logistic.client.hub.domain.exception;

import com.logistic.client.hub.application.exception.CustomException;
import com.logistic.client.hub.application.exception.HubExceptionCode;

public class HubAlreadyDeletedException extends CustomException {
  public HubAlreadyDeletedException(HubExceptionCode code) {
    super(code);
  }
}
