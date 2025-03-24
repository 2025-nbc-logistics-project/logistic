package com.logistic.client.user.infrastructure.configuration;

import com.logistic.client.user.infrastructure.configuration.customException.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> illegalArgumentException(IllegalArgumentException ex) {
        int status = HttpServletResponse.SC_BAD_REQUEST;
        ExceptionResponse response = new ExceptionResponse("Bad Request", ex.getMessage(), status);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResponse> nullPointerException(NullPointerException ex) {
        int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        ExceptionResponse response = new ExceptionResponse("INTERNAL_SERVER_ERROR", ex.getMessage(), status);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> forbiddenException(ForbiddenException ex) {
        int status = HttpServletResponse.SC_FORBIDDEN;
        ExceptionResponse response = new ExceptionResponse("FORBIDDEN", ex.getMessage(), status);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> accessDeniedException(AccessDeniedException ex) {
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        ExceptionResponse response = new ExceptionResponse("ACCESS_DENIED", ex.getMessage(), status);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(CompanyDeliveryManagerCountMaxException.class)
    public ResponseEntity<ExceptionResponse> companyDeliveryManagerCountMaxException(CompanyDeliveryManagerCountMaxException ex) {
        int status = HttpServletResponse.SC_BAD_REQUEST;
        ExceptionResponse response = new ExceptionResponse("COUNT_MAX", ex.getMessage(), status);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(HubDeliveryManagerCountMaxException.class)
    public ResponseEntity<ExceptionResponse> hubDeliveryManagerCountMaxException(HubDeliveryManagerCountMaxException ex) {
        int status = HttpServletResponse.SC_BAD_REQUEST;
        ExceptionResponse response = new ExceptionResponse("COUNT_MAX", ex.getMessage(), status);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<ExceptionResponse> samePasswordException(SamePasswordException ex) {
        int status = HttpServletResponse.SC_BAD_REQUEST;
        ExceptionResponse response = new ExceptionResponse("SAME_PASSWORD", ex.getMessage(), status);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(NotDeliveryManagerException.class)
    public ResponseEntity<ExceptionResponse> notDeliveryManagerException(NotDeliveryManagerException ex) {
        int status = HttpServletResponse.SC_NOT_FOUND;
        ExceptionResponse response = new ExceptionResponse("NOT_DELIVERY_MANAGER", ex.getMessage(), status);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> userAlreadyExistException(UserAlreadyExistException ex) {
        int status = HttpServletResponse.SC_BAD_REQUEST;
        ExceptionResponse response = new ExceptionResponse("ALREADY_EXIST_USER", ex.getMessage(), status);
        return ResponseEntity.status(status).body(response);
    }
}
