package com.logistic.client.user.presentation.controller;

import com.logistic.client.user.infrastructure.Security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token, @RequestHeader("requestUri") String requestUri) {
        try {
            if (requestUri.equals("/api/v1/users/signup") || requestUri.equals("/api/v1/users/signin")) {
                return ResponseEntity.ok().build();  // 인증 없이 진행
            }

            HttpHeaders headers = authService.validateToken(token);
            return ResponseEntity.ok().headers(headers).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }
    }

}
