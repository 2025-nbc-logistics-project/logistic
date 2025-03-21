package com.logistic.client.alarm.infrastructure.repository;

import com.logistic.client.alarm.domain.model.Slack;
import com.logistic.client.alarm.domain.repository.SlackRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SlackRepositoryImpl implements SlackRepository {

    private final JpaSlackRepository jpaSlackRepository;

    public SlackRepositoryImpl(JpaSlackRepository jpaSlackRepository) {
        this.jpaSlackRepository = jpaSlackRepository;
    }

    @Override
    public Slack save(Slack slack) {
        return jpaSlackRepository.save(slack);
    }

    @Override
    public Optional<Slack> findById(UUID slackId) {
        return jpaSlackRepository.findById(slackId);
    }
}
