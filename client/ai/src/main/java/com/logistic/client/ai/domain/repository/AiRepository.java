package com.logistic.client.ai.domain.repository;

import com.logistic.client.ai.domain.model.Ai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AiRepository {
    void save(Ai ai);
    Page<Ai> getAis(Pageable pageable, String sortBy, String order);
}
