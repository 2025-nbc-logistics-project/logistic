package com.logistic.client.company.infrastructure.repository.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductQueryDSLRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

}
