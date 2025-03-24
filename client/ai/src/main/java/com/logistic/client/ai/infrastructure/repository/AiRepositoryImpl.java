package com.logistic.client.ai.infrastructure.repository;

import com.logistic.client.ai.domain.model.Ai;
import com.logistic.client.ai.domain.repository.AiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiRepositoryImpl implements AiRepository {

    private final JpaAiRepository jpaAiRepository;
    private final AiQueryDSLRepositoryImpl aiQueryDSLRepository;

    @Override
    public void save(Ai ai) {
        jpaAiRepository.save(ai);
    }

    @Override
    public Page<Ai> getAis(Pageable pageable, String sortBy, String order) {
        return aiQueryDSLRepository.getAis(pageable, sortBy, order);
    }
}
