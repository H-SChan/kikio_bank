package com.kakao.bank.domain.response.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRo {
    private String token;

    public LoginRo(String token) {
        this.token = token;
    }
}
