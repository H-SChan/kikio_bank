package com.kakao.bank.domain.dto.user.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class SimpleCertify {
    @NotBlank
    private int number;
}
