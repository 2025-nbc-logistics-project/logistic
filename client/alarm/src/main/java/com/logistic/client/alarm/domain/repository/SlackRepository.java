package com.logistic.client.alarm.domain.repository;

import com.logistic.client.alarm.domain.model.Slack;

import java.util.Optional;
import java.util.UUID;

public interface SlackRepository {
    Slack save(Slack slack);
    Optional<Slack> findById(UUID slackId);
}
