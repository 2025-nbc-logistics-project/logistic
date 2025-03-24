package com.logistic.client.user.infrastructure.repository;

import com.logistic.client.user.domain.model.User;
import com.logistic.client.user.domain.model.UserRole;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID>, QuerydslPredicateExecutor<User> {
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    List<User> findAllByIsDeletedFalse();
    Page<User> findAllByIsDeletedFalse(PageRequest pageable);
    Boolean existsByUsernameAndIsDeletedFalse(String username);
    Page<User> findAllByRoleAndIsDeletedFalse(UserRole role, PageRequest pageable);
    Optional<User> findByHubIdAndIsDeletedFalse(UUID hubId);

}
