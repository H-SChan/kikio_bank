package com.kakao.bank.domain.dto.account.request;

import com.kakao.bank.domain.enums.Bank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RemittanceDto {
    private Bank toBank;
    private Long toMoney;
    private String toName;
    private String toAccountNumber;
    private String fromAccountNumber;
}
