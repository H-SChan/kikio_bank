package com.kakao.bank.domain.dto.communication;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DepositDto {
    private String accountNumber;
    private String name;
    private Long money;

    public DepositDto(String accountNumber, String name, Long money) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.money = money;
    }
}
