package com.kakao.bank.domain.response.communication;

import com.kakao.bank.domain.entity.Account;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetAccountListRo {
    private String accountNumber;
    private Long money;
    private String password;
    private String nickname;

    public GetAccountListRo(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.money = account.getMoney();
        this.password = account.getPassword();
        this.nickname = account.getNickname();
    }
}
