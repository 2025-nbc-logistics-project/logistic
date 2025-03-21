package com.logistic.client.user.infrastructure.Security;

import com.logistic.client.user.domain.model.UserRole;
import com.logistic.client.user.infrastructure.Security.dto.CreateTokenDTO;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtImpl jwtImpl;

    public String createAccessToken(CreateTokenDTO request) {
        return jwtImpl.createAccessToken(request.getUsername(), request.getRole());
    }

    public HttpHeaders validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token");
        }
        token = token.substring(7);
        try {
            Claims claims = jwtImpl.getClaims(token);
            String username = claims.get("username", String.class);
            String roleString = claims.get("role", String.class);

            UserRole role = UserRole.valueOf(roleString);

            HttpHeaders headers = new HttpHeaders();
            // 검증된 정보를 응답 헤더에 추가
            headers.add("username", username);
            headers.add("role", role.toString());

            return headers;

        } catch (Exception e) {
            throw new RuntimeException("JWT 검증 실패: " + e.getMessage());
        }
    }

}
