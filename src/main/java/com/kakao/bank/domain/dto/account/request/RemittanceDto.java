package com.kakao.bank.domain.dto.account.request;

import com.kakao.bank.domain.enums.Bank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RemittanceDto {
    private Bank bank;
    private Long money;
    private String toName;
}
