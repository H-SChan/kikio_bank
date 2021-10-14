package com.kakao.bank.domain.response.account;

import com.kakao.bank.domain.entity.Account;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountRo {
    private String accountNumber;
    private String nickname;
    private Long money;

    public void accountToAccountRo(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.nickname = account.getNickname();
        this.money = account.getMoney();
    }
}
