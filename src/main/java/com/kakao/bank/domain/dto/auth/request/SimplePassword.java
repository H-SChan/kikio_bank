package com.kakao.bank.domain.dto.auth.request;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class SimplePassword {
    @Pattern(regexp = "^[0-9]*$", message = "숫자만 입력 가능 합니다.")
    private Integer password;
}
