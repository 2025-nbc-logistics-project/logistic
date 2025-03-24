package com.logistic.client.user.presentation.controller;

import com.logistic.client.user.infrastructure.Security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name ="Auth API", description = "인증 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "토큰 유효성 검사", description = "헤더에서 토큰을 추출하여 토큰의 유효성 검사 후 인증 정보 전달")
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
