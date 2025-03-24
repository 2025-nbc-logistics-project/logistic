package com.logistic.client.ai.infrastructure.repository;

import com.logistic.client.ai.domain.model.Ai;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaAiRepository extends JpaRepository<Ai, UUID> {
}
