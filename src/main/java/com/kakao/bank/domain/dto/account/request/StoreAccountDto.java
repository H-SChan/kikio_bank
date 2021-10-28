package com.kakao.bank.domain.dto.account.request;

import com.kakao.bank.domain.enums.Bank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StoreAccountDto {
    private String accountNumber;
    private String nickname;
    private Long money;
    private Bank bank;
}
