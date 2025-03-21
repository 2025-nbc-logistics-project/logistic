package com.logistic.client.alarm.domain.repository;

import com.logistic.client.alarm.domain.model.Slack;
import com.logistic.client.alarm.presentation.request.SlackSearchDto;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface SlackRepository {
    void save(Slack slack);
    Optional<Slack> findById(UUID slackId);

    Page<Slack> searchSlack(SlackSearchDto requestDto);
}
