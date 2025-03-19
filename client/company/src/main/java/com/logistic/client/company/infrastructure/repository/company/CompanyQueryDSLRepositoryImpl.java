package com.logistic.client.company.infrastructure.repository.company;

import com.logistic.client.company.domain.model.Company;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public Page<Company> getCompanies(Pageable pageable, String sortBy, String order) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, order);
        List<Company> companyList = jpaQueryFactory
                .selectFrom(company)
                .where(company.deletedAt.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();
        long total = jpaQueryFactory
                .select(company.count())
                .from(company)
                .where(company.deletedAt.isNull())
                .fetchOne();

        return new PageImpl<>(companyList, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy, String order) {
        Order direction = order.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;
        if ("updatedAt".equals(sortBy)) {
            return new OrderSpecifier<>(direction, company.updatedAt);
        } else {
            return new OrderSpecifier<>(direction, company.createdAt);
        }
    }
}
