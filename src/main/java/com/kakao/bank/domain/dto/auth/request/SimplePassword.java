package com.kakao.bank.domain.dto.auth.request;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class SimplePassword {
    private Integer password;
}
