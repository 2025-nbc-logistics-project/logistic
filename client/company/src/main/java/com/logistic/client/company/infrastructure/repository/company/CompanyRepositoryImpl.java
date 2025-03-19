package com.logistic.client.company.infrastructure.repository.company;

import com.logistic.client.company.domain.model.Company;
import com.logistic.client.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepository {

    private final JpaCompanyRepository jpaCompanyRepository;
    private final CompanyQueryDSLRepositoryImpl companyQueryDSLRepository;

    @Override
    public void save(Company company) {
        jpaCompanyRepository.save(company);
    }

    @Override
    public boolean isDuplicateStore(String companyName, String companyTel) {
        return companyQueryDSLRepository.isDuplicateStore(companyName, companyTel);
    }

}
