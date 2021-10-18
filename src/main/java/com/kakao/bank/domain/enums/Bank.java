package com.kakao.bank.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Bank {
    KAKAO("108", "새찬"),
    KBANK("110", "장우"),
    TOSS("666", "승도"),
    DAEGU("031", "승우"),
    MAAGU("719", "지나");


    private final String bankNum;
    private final String name;
}
