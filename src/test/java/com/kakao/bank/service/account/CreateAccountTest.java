package com.kakao.bank.service.account;

import com.kakao.bank.domain.dto.account.request.OpeningAccountDto;
import com.kakao.bank.domain.enums.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CreateAccountTest {
    @Autowired
    private AccountService accountService;

    @Test
    void checkRandomAccountNum() {
        accountService.openingAccount(new OpeningAccountDto(AccountType.BASIC, "1234", "자유"), "qwer1234");
    }
}
