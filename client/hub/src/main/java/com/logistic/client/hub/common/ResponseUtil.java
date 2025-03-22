package com.logistic.client.hub.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ResponseUtil {

  public static <T> ResponseEntity<ApiResponse<T>> success(T body) {
    ApiResponse<T> response = buildResponse(body);
    logResponse(response);
    return ResponseEntity.ok(response);
  }

  public static ResponseEntity<ApiResponse<Void>> noContent() {
    ApiResponse<Void> response = buildResponse(null);
    logResponse(response);
    return ResponseEntity.noContent().build();
  }

  public static ResponseEntity<String> notFound(String message) {
    log.error("Not found error: {}", message);
    return buildErrorResponse(message, HttpStatus.NOT_FOUND);
  }

  public static ResponseEntity<String> badRequest(String message) {
    log.error("Bad request error: {}", message);
    return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
  }

  public static <T> ResponseEntity<T> customResponse(HttpStatus status, T body) {
    log.info("Custom response: {}", body);
    return ResponseEntity.status(status).body(body);
  }

  public static ResponseEntity<String> internalServerError(String message) {
    log.error("Internal server error: {}", message);
    return buildErrorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private static <T> ApiResponse<T> buildResponse(T body) {
    return new ApiResponse<>("success", body);
  }

  private static ResponseEntity<String> buildErrorResponse(String message, HttpStatus status) {
    return ResponseEntity.status(status).body(message);
  }

  private static <T> void logResponse(ApiResponse<T> response) {
    log.info("Response: {}", response);
  }

}