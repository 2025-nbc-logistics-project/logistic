package com.logistic.client.alarm.infrastructure.repository;

import com.logistic.client.alarm.domain.model.Slack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaSlackRepository extends JpaRepository<Slack, UUID> {
}
