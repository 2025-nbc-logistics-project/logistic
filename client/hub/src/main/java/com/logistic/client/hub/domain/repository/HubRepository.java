package com.logistic.client.hub.domain.repository;

import com.logistic.client.hub.domain.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubRepository extends JpaRepository<Hub, Long> {

  boolean existsByName(String name);

}
