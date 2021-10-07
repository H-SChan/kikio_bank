package com.kakao.bank.domain.response.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AvailableIdRo {
    private Boolean available;

    public AvailableIdRo(Boolean available) {
        this.available = available;
    }
}
