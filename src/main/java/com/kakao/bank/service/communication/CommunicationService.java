package com.kakao.bank.service.communication;

import com.kakao.bank.domain.dto.communication.BroughtAccountDto;
import com.kakao.bank.domain.dto.communication.CheckAccountPasswordDto;
import com.kakao.bank.domain.response.communication.CheckAccountNumRo;
import com.kakao.bank.domain.response.communication.GetAccountListRo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommunicationService {
    @Transactional(readOnly = true)
    List<GetAccountListRo> getMyAccounts(String phoneNumber);

    @Transactional(readOnly = true)
    CheckAccountNumRo validAccount(String accountNumber);

    List<BroughtAccountDto> getOtherAccounts(String phoneNumber);

    @Transactional(readOnly = true)
    Boolean checkPassword(CheckAccountPasswordDto dto);
}
