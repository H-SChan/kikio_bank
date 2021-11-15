package com.kakao.bank.domain.dto.communication;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CheckAccountPasswordDto {
    private String accountNum;
    private String password;
}
