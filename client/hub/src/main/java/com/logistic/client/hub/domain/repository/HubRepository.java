package com.logistic.client.hub.domain.repository;

import com.logistic.client.hub.domain.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HubRepository extends JpaRepository<Hub, Long> , JpaSpecificationExecutor<Hub> {

  boolean existsByName(String name);

}
