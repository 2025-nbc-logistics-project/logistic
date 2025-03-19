package com.logistic.client.company.infrastructure.repository.company;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.logistic.client.company.domain.model.QCompany.company;

@Repository
@RequiredArgsConstructor
public class CompanyQueryDSLRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    public boolean isDuplicateStore(String companyName, String companyTel) {
        long count = jpaQueryFactory
                .select(company.count())
                .from(company)
                .where(
                        company.companyName.eq(companyName),
                        company.companyTel.eq(companyTel),
                        company.deletedAt.isNull()
                )
                .fetchOne();
        return count > 0;
    }
}
