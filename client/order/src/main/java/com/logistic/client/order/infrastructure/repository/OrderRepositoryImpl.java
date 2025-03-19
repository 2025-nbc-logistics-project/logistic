package com.logistic.client.order.infrastructure.repository;

import com.logistic.client.order.application.dto.OrderSearchDto;
import com.logistic.client.order.domain.model.Order;
import com.logistic.client.order.domain.model.QOrder;
import com.logistic.client.order.domain.repository.OrderRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager em;

    private final JpaOrderRepository jpaOrderRepository;
    private final JPAQueryFactory qf;
    private final QOrder order = QOrder.order;

    public OrderRepositoryImpl(JpaOrderRepository jpaOrderRepository, EntityManager em) {
        this.jpaOrderRepository = jpaOrderRepository;
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public void save(Order order) {
        jpaOrderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return jpaOrderRepository.findById(orderId);
    }

    @Override
    public Page<Order> searchOrders(OrderSearchDto searchDto) {
        Sort.Direction direction = (searchDto.getIsAsc()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, searchDto.getSortBy());
        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), sort);

        List<Order> content = qf
            .selectFrom(order)
            .where(buildWhereClause(searchDto))
            .orderBy(buildSortClause(searchDto))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = qf.selectFrom(order).fetch().size();

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }

    private OrderSpecifier<?> buildSortClause(OrderSearchDto searchDto) {
        ComparableExpressionBase<?> sortPath;

        if ("updatedAt".equalsIgnoreCase(searchDto.getSortBy())) {
            sortPath = order.updatedAt;
        } else {
            sortPath = order.createdAt;
        }

        if (searchDto.getIsAsc()) {
            return sortPath.asc();
        } else {
            return sortPath.desc();
        }
    }

    private Predicate buildWhereClause(OrderSearchDto searchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(order.isDeleted.eq(false));

        if (searchDto.getSupplierCompanyId() != null) {
            builder.and(order.companyInfo.supplierCompanyId.eq(searchDto.getSupplierCompanyId()));
        }
        if (searchDto.getReceiverCompanyId() != null) {
            builder.and(order.companyInfo.receiverCompanyId.eq(searchDto.getReceiverCompanyId()));
        }
        if (searchDto.getUserId() != null) {
            builder.and(order.createdBy.eq(searchDto.getUserId()));
        }
        return builder.getValue();
    }


}
