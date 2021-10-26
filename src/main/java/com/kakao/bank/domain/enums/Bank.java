package com.kakao.bank.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Bank {
    KAKAO("KAKAO", "108", "새찬"),
    KBANK("KBANK", "110", "장우"),
    TOSS("TOSS", "666", "승도"),
    DAEGU("DAEGU", "031", "승우"),
    MAAGU("MAAGU", "719", "지나");

    private final String enumName;
    private final String bankNum;
    private final String name;
}
