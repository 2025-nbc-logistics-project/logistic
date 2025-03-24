package com.logistic.client.user.infrastructure.repository;

import com.logistic.client.user.domain.model.User;
import com.logistic.client.user.domain.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Boolean existsByUsernameAndIsDeletedFalse(String username) {
        return jpaUserRepository.existsByUsernameAndIsDeletedFalse(username);
    }

    @Override
    public Page<User> findAll(BooleanBuilder builder, Pageable pageable) {
        return jpaUserRepository.findAll(builder, pageable);
    }

    @Override
    public Optional<User> findByHubIdAndIsDeletedFalse(UUID hubId) {
        return jpaUserRepository.findByHubIdAndIsDeletedFalse(hubId);
    }
}
