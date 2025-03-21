package com.logistic.client.alarm.infrastructure.repository;

import com.logistic.client.alarm.domain.model.QMessage;
import com.logistic.client.alarm.domain.model.QSlack;
import com.logistic.client.alarm.domain.model.Slack;
import com.logistic.client.alarm.domain.repository.SlackRepository;
import com.logistic.client.alarm.presentation.request.SlackSearchDto;
import com.querydsl.core.types.OrderSpecifier;
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
public class SlackRepositoryImpl implements SlackRepository {

    @PersistenceContext
    private EntityManager em;

    private final JpaSlackRepository jpaSlackRepository;
    private final JPAQueryFactory qf;
    private final QSlack slack = QSlack.slack;
    private final QMessage message = QMessage.message;

    public SlackRepositoryImpl(JpaSlackRepository jpaSlackRepository, EntityManager em) {
        this.jpaSlackRepository = jpaSlackRepository;
        this.qf = new JPAQueryFactory(em);
    }

    @Override
    public void save(Slack slack) {
        jpaSlackRepository.save(slack);
    }

    @Override
    public Optional<Slack> findById(UUID slackId) {
        return jpaSlackRepository.findById(slackId);
    }

    @Override
    public Page<Slack> searchSlack(SlackSearchDto requestDto) {
        Sort.Direction direction = (requestDto.getIsAsc()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, requestDto.getSortBy());
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize(), sort);

        List<Slack> content = qf
            .selectFrom(slack)
            .where(slack.isDeleted.eq(false),
                message.text.contains(requestDto.getKeyword()))
            .orderBy(buildSortClause(requestDto))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = qf.selectFrom(slack).fetch().size();

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }

    private OrderSpecifier<?> buildSortClause(SlackSearchDto requestDto) {
        ComparableExpressionBase<?> sortPath;

        if ("updatedAt".equalsIgnoreCase(requestDto.getSortBy())) {
            sortPath = slack.updatedAt;
        } else {
            sortPath = slack.createdAt;
        }

        if (requestDto.getIsAsc()) {
            return sortPath.asc();
        } else {
            return sortPath.desc();
        }
    }
}
