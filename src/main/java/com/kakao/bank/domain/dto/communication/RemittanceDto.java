package com.kakao.bank.domain.dto.communication;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RemittanceDto {
    private String password;
    private String name;
    private Long money;
    private String accountNumber;
}
