package com.kakao.bank.domain.response.account;

import com.kakao.bank.domain.entity.Account;
import com.kakao.bank.domain.enums.AccountType;
import com.kakao.bank.domain.response.record.RecordRo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class DetailAccountRo {
    private String accountNumber;
    private String nickname;
    private Long money;
    private AccountType type;
    private List<RecordRo> records;

    public void accountToDetailAccountRo(Account account, AccountType accountType) {
        this.accountNumber = account.getAccountNumber();
        this.nickname = account.getNickname();
        this.money = account.getMoney();
        this.type = accountType;
    }

}
