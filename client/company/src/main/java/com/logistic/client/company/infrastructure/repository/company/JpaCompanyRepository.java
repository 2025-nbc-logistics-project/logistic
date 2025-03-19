package com.logistic.client.company.infrastructure.repository.company;

import com.logistic.client.company.domain.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaCompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByCompanyIdAndDeletedAtIsNull(UUID companyId);
}
