package com.kakao.bank.service.account;

import com.kakao.bank.domain.dto.account.request.OpeningAccountDto;
import com.kakao.bank.domain.enums.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class CreateAccountTest {
    @Autowired
    private AccountService accountService;

    @Test
    @Rollback
    void checkRandomAccountNum() {
        accountService.openingAccount(new OpeningAccountDto(AccountType.BASIC, "1234", "__통장"), "contest2");
    }
}
