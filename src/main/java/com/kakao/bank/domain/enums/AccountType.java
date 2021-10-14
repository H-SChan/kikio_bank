package com.kakao.bank.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    BASIC("입출금", "10"),
    DEPOSIT("예금", "20"),
    INSTALLMENT_SAVING("적금", "30");

    // 종류
    private final String type;
    // 식별 번호
    private final String identificationNum;
}
