package com.kakao.bank.domain.dto.auth.request;

import lombok.Getter;

@Getter
public class LoginReq {
    private String id;
    private String password;
}
