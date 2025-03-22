package com.logistic.client.user.infrastructure.Security;

import com.logistic.client.user.domain.model.User;
import com.logistic.client.user.domain.model.UserRole;
import com.logistic.client.user.infrastructure.Security.JwtImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityContextFilter extends OncePerRequestFilter {

    private final JwtImpl jwtImpl;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private static final List<String> excludeUrls = List.of(
            "/api/v1/users/signup",
            "/api/v1/users/signin"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        return excludeUrls.stream().anyMatch(url -> pathMatcher.match(url, requestPath));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String headerAuthorizationToken = request.getHeader(AUTHORIZATION_HEADER);

        // JWT 토큰이 없거나 Bearer 접두어가 없는 경우
        if (headerAuthorizationToken == null || !headerAuthorizationToken.startsWith(BEARER_PREFIX)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.print("No authentication information found. Please log in.");
            return;
        }

        String token = headerAuthorizationToken.substring(7);

        try {
            UUID userId = jwtImpl.getUserId(token);
            String username = jwtImpl.getUsername(token);
            String role = String.valueOf(jwtImpl.getUserRole(token));
            String slackId = jwtImpl.getSlackId(token);

            User user = User.builder()
                    .userId(userId)
                    .role(UserRole.valueOf(role))
                    .slackId(slackId)
                    .username(username)
                    .build();

            PrincipalDetails userDetails = new PrincipalDetails(user);

            // 스프링 시큐리티 인증 토큰 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null,
                    userDetails.getAuthorities());

            // 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            PrintWriter writer = response.getWriter();
            writer.print("Access token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (MalformedJwtException e) {
            PrintWriter writer = response.getWriter();
            writer.print("Malformed JWT token");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            return;
        } catch (JwtException e) {
            PrintWriter writer = response.getWriter();
            writer.print("Invalid token claims");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            return;
        }   catch (Exception e) {
            PrintWriter writer = response.getWriter();
            writer.print("Error processing JWT token");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            return;
        }

        filterChain.doFilter(request, response);
    }
}