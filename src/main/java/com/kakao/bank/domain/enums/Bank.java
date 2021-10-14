package com.kakao.bank.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Bank {
    SAECHAN("108");

    private final String bankNum;
}
