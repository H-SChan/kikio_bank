package com.kakao.bank.domain.dto.account.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TakeMoneyDto {
    private String fromAccountNum;
    private String toAccountNum;
    private Long money;
}
