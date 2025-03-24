package com.logistic.client.ai.infrastructure.repository;

import com.logistic.client.ai.domain.model.Ai;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.logistic.client.ai.domain.model.QAi.ai;

@Repository
@RequiredArgsConstructor
public class AiQueryDSLRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Ai> getAis(Pageable pageable, String sortBy, String order) {

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, order);

        List<Ai> productList = jpaQueryFactory
                .selectFrom(ai)
                .where(ai.deletedAt.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();

        long total = jpaQueryFactory
                .select(ai.count())
                .from(ai)
                .where(ai.deletedAt.isNull())
                .fetchOne();

        return new PageImpl<>(productList, pageable, total);

    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy, String order) {

        Order direction = order.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;

        if ("updatedAt".equals(sortBy)) {
            return new OrderSpecifier<>(direction, ai.updatedAt);
        } else {
            return new OrderSpecifier<>(direction, ai.createdAt);
        }
    }
}
