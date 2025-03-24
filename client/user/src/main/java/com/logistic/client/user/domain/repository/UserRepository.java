package com.logistic.client.user.domain.repository;

import com.logistic.client.user.domain.model.User;
import com.logistic.client.user.domain.model.UserRole;
import com.querydsl.core.BooleanBuilder;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    List<User> findAllByIsDeletedFalse();
    User save(User user);
    Boolean existsByUsernameAndIsDeletedFalse(String username);
    Page<User> findAllByRoleAndIsDeletedFalse(UserRole role, PageRequest pageable);
    Optional<User> findByHubIdAndIsDeletedFalse(UUID hubId);
}
