package com.kakao.bank.domain.repository;

import com.kakao.bank.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long> {
}
