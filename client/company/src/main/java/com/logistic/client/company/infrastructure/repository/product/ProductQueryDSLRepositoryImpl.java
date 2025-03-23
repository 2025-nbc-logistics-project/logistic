package com.logistic.client.company.infrastructure.repository.product;

import com.logistic.client.company.domain.model.product.Product;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.logistic.client.company.domain.model.product.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductQueryDSLRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Product> getProducts(Pageable pageable, String sortBy, String order) {

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, order);

        List<Product> productList = jpaQueryFactory
                .selectFrom(product)
                .where(product.deletedAt.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();

        long total = jpaQueryFactory
                .select(product.count())
                .from(product)
                .where(product.deletedAt.isNull())
                .fetchOne();

        return new PageImpl<>(productList, pageable, total);
    }

    public Page<Product> getSearchProducts(String key, UUID company, UUID hub, Pageable pageable, String sortBy, String order) {

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, order);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(product.deletedAt.isNull());

        if(key != null && !key.isBlank()) {
            builder.and(product.productInfo.productName.containsIgnoreCase(key));
        }

        if(company != null) {
            builder.and(product.companyId.eq(company));
        }

        if(hub != null) {
            builder.and(product.hubId.eq(hub));
        }

        List<Product> productList = jpaQueryFactory
                .selectFrom(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();

        long total = jpaQueryFactory
                .select(product.count())
                .from(product)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(productList, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy, String order) {

        Order direction = order.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;

        if ("updatedAt".equals(sortBy)) {
            return new OrderSpecifier<>(direction, product.updatedAt);
        } else {
            return new OrderSpecifier<>(direction, product.createdAt);
        }
    }

}
