package com.logistic.client.company.domain.repository;

import com.logistic.client.company.domain.model.Company;

public interface CompanyRepository {
    void save(Company company);
    boolean isDuplicateStore(String companyName, String companyTel);
}
