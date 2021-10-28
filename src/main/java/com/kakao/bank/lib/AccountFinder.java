package com.kakao.bank.lib;

import com.kakao.bank.domain.entity.Account;
import com.kakao.bank.domain.repository.AccountRepo;
import com.kakao.bank.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccountFinder {

    private final AccountRepo accountRepo;

    public Account accountNumber(String accountNumber) {
        return accountRepo.findAccountByAccountNumber(accountNumber).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 계좌")
        );
    }
}
