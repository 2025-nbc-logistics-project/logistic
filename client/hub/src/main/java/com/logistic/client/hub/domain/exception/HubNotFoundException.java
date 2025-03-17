package com.logistic.client.hub.domain.exception;

import com.logistic.client.hub.application.exception.CustomException;
import com.logistic.client.hub.application.exception.HubExceptionCode;

public class HubNotFoundException extends CustomException {
  public HubNotFoundException(HubExceptionCode code) {
    super(code);
  }
}
