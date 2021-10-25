package com.kakao.bank.domain.repository;

import com.kakao.bank.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Long> {
    public Optional<Account> findAccountByAccountNumber(String accountNumber);
}
