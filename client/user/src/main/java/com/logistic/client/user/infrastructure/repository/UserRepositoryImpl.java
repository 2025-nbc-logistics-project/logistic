package com.logistic.client.user.infrastructure.repository;

import com.logistic.client.user.domain.model.QUser;
import com.logistic.client.user.domain.model.User;
import com.logistic.client.user.domain.model.UserRole;
import com.logistic.client.user.domain.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    @Override
    public Optional<User> findByUsernameAndIsDeletedFalse(String username) {
        return jpaUserRepository.findByUsernameAndIsDeletedFalse(username);
    }

    @Override
    public List<User> findAllByIsDeletedFalse() {
        return jpaUserRepository.findAllByIsDeletedFalse();
    }

    @Override
    public Page<User> findAllByIsDeletedFalse(PageRequest pageable) {
        return jpaUserRepository.findAllByIsDeletedFalse(pageable);
    }

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Boolean existsByUsernameAndIsDeletedFalse(String username) {
        return jpaUserRepository.existsByUsernameAndIsDeletedFalse(username);
    }

    @Override
    public Page<User> findAllByRoleAndIsDeletedFalse(UserRole role, PageRequest pageable) {
        return jpaUserRepository.findAllByRoleAndIsDeletedFalse(role, pageable);
    }

    @Override
    public Optional<User> findByHubIdAndIsDeletedFalse(UUID hubId) {
        return jpaUserRepository.findByHubIdAndIsDeletedFalse(hubId);
    }
}
