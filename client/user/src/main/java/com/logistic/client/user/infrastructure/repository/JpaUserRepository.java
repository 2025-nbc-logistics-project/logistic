package com.logistic.client.user.infrastructure.repository;

import com.logistic.client.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    List<User> findAllByIsDeletedFalse();
    Boolean existsByUsernameAndIsDeletedFalse(String username);

}
