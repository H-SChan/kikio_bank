package com.kakao.bank.domain.response.communication;

import com.kakao.bank.domain.entity.Account;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CheckAccountNumRo {
    private String message;
    private String userName = null;
    private String accountNum = null;

    public CheckAccountNumRo(String message, Account account) {
        this.message = message;
        this.userName = account.getUser().getName();
        this.accountNum = account.getAccountNumber();
    }
}
