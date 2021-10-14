package com.kakao.bank.domain.response.account;

import com.kakao.bank.domain.entity.Account;
import com.kakao.bank.domain.enums.Bank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountRo {
    private Long idx;
    private String accountNumber;
    private String nickname;
    private Long money;
    private Bank kindOfBank;

    public void accountToAccountRo(Account account, Bank bank) {
        this.idx = account.getIdx();
        this.accountNumber = account.getAccountNumber();
        this.nickname = account.getNickname();
        this.money = account.getMoney();
        this.kindOfBank = bank;
    }
}
