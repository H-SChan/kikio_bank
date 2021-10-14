package com.kakao.bank.service.account;

import com.kakao.bank.domain.dto.account.request.OpeningAccountDto;

public interface AccountService {
    void openingAccount(OpeningAccountDto dto, String userId);
}
