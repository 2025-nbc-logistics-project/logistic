package com.logistic.client.hub.domain.repository;

import com.logistic.client.hub.domain.model.HubRoute;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubRouteRepository extends JpaRepository<HubRoute, UUID> {

}