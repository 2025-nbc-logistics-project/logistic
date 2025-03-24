package com.logistic.client.ai.infrastructure.configuration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<UUID> {

    private final HttpServletRequest request;


    @Override
    public Optional<UUID> getCurrentAuditor() {

        String requestUserId = request.getHeader("userId");
        UUID userId = null;

        if(requestUserId != null && !requestUserId.isBlank()) {
            userId = UUID.fromString(requestUserId);
        }

        return Optional.ofNullable(userId);
    }

}
