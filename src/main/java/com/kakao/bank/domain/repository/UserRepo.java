package com.kakao.bank.domain.repository;

import com.kakao.bank.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
