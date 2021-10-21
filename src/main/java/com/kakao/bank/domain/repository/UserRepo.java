package com.kakao.bank.domain.repository;

import com.kakao.bank.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findById(String id);
    Optional<User> findUserByPhoneNumber(String phoneNumber);
}
