package com.kakao.bank.domain.dto.communication;

import com.kakao.bank.domain.enums.Bank;
import lombok.Data;

@Data
public class BroughtAccountDto {
    private String accountNumber;
    private String nickName;
    private Long money;
    private String password;
    private Bank bank;

    public BroughtAccountDto(String accountNumber, String nickName, Long money, String password, Bank bank) {
        this.accountNumber = accountNumber;
        this.nickName = nickName;
        this.money = money;
        this.password = password;
        this.bank = bank;
    }
}
