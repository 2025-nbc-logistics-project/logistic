package com.logistic.client.user.infrastructure.configuration;

import com.logistic.client.user.infrastructure.Security.JwtImpl;
import com.logistic.client.user.infrastructure.Security.SecurityContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtImpl jwtImpl;

    public SecurityConfig(JwtImpl jwtImpl) {
        this.jwtImpl = jwtImpl;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.addFilterBefore(new SecurityContextFilter(jwtImpl), UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorization -> {
            // 공용 URL (인증 없이 접근 가능)
            authorization.requestMatchers(
                    "/api/v1/users/signup",
                    "/api/v1/users/signin",
                    "/api/v1/auth/validate",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html"
            ).permitAll();

            authorization.anyRequest().authenticated();
        });
        return http.build();
    }
}
