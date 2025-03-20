package com.logistic.client.order.infrastructure.repository;

import com.logistic.client.order.application.dto.DeliverySearchDto;
import com.logistic.client.order.domain.model.Delivery;
import com.logistic.client.order.domain.model.QDelivery;
import com.logistic.client.order.domain.repository.DeliveryRepository;
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
public class DeliveryRepositoryImpl implements DeliveryRepository {

    @PersistenceContext
    private EntityManager em;

    private final JpaDeliveryRepository jpaDeliveryRepository;
    private final JPAQueryFactory qf;
    private final QDelivery delivery = QDelivery.delivery;

    public DeliveryRepositoryImpl(JpaDeliveryRepository jpaDeliveryRepository, EntityManager em) {
        this.jpaDeliveryRepository = jpaDeliveryRepository;
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public void save(Delivery delivery) {
        jpaDeliveryRepository.save(delivery);
    }

    @Override
    public Optional<Delivery> findById(UUID deliveryId) {
        return jpaDeliveryRepository.findById(deliveryId);
    }

    @Override
    public Page<Delivery> searchDeliveries(DeliverySearchDto searchDto) {
        Sort.Direction direction = (searchDto.getIsAsc()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, searchDto.getSortBy());
        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), sort);

        List<Delivery> content = qf
            .selectFrom(delivery)
            .where(buildWhereClause(searchDto))
            .orderBy(buildSortClause(searchDto))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = qf.selectFrom(delivery).fetch().size();

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }

    private OrderSpecifier<?> buildSortClause(DeliverySearchDto searchDto) {
        ComparableExpressionBase<?> sortPath;

        if ("updatedAt".equalsIgnoreCase(searchDto.getSortBy())) {
            sortPath = delivery.updatedAt;
        } else {
            sortPath = delivery.createdAt;
        }

        if (searchDto.getIsAsc()) {
            return sortPath.asc();
        } else {
            return sortPath.desc();
        }
    }

    private Predicate buildWhereClause(DeliverySearchDto searchDto) {
        BooleanBuilder builder = new BooleanBuilder();
        // 논리 삭제 제외
        builder.and(delivery.isDeleted.eq(false));

        if (searchDto.getSupplierDeliveryManagerId() != null) {
            builder.and(delivery.deliveryManagerId.supplierDeliveryManagerId.eq(searchDto.getSupplierDeliveryManagerId()));
        }
        if (searchDto.getReceiverDeliveryManagerId() != null) {
            builder.and(delivery.deliveryManagerId.receiverDeliveryManagerId.eq(searchDto.getReceiverDeliveryManagerId()));
        }

        return builder.getValue();
    }
}
