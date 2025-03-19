package com.logistic.client.company.infrastructure.repository.company;

import com.logistic.client.company.domain.model.Company;
import com.logistic.client.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Optional<Company> findByCompanyIdAndDeletedAtIsNull(UUID companyId) {
        return jpaCompanyRepository.findByCompanyIdAndDeletedAtIsNull(companyId);
    }

    @Override
    public Page<Company> getCompanies(Pageable pageable, String sortBy, String order) {
        return companyQueryDSLRepository.getCompanies(pageable, sortBy, order);
    }

}
