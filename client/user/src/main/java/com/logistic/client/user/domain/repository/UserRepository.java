package com.logistic.client.user.domain.repository;

import com.logistic.client.user.domain.model.User;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    List<User> findAllByIsDeletedFalse();
    User save(User user);
    Boolean existsByUsernameAndIsDeletedFalse(String username);
    Page<User> findAll(BooleanBuilder builder, Pageable pageable);
    Optional<User> findByHubIdAndIsDeletedFalse(String username);

}
