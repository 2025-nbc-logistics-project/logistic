package com.logistic.client.company.domain.repository;

import com.logistic.client.company.domain.model.company.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    void save(Company company);
    boolean isDuplicateStore(String companyName, String companyTel);
    Optional<Company> findByCompanyIdAndDeletedAtIsNull(UUID companyId);
    Page<Company> getCompanies(Pageable pageable, String sortBy, String order);
    Page<Company> getSearchCompanies(String key, Pageable pageable, String sortBy, String order);
}
