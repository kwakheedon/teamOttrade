package com.ottrade.ottrade.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ottrade.ottrade.domain.member.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findByPhone(String phone);

	boolean existsByNickname(String nickname);

}
