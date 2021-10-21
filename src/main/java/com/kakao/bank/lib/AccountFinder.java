package com.kakao.bank.lib;

import com.kakao.bank.domain.entity.Account;
import com.kakao.bank.domain.repository.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@RequiredArgsConstructor
@Component
public class AccountFinder {

    private final AccountRepo accountRepo;

    public Account accountNumber(String accountNumber) {
        return accountRepo.findAccountByAccountNumber(accountNumber).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "없는 계좌")
        );
    }
}
