package com.kakao.bank.domain.dto.account.request;

import com.kakao.bank.domain.enums.AccountType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class OpeningAccountDto {

    @NotBlank
    private AccountType accountType;

    @NotBlank
    private String password;

    private String nickname;

    public OpeningAccountDto(@NotBlank AccountType accountType, @NotBlank String password, String nickname) {
        this.accountType = accountType;
        this.password = password;
        this.nickname = nickname;
    }
}
