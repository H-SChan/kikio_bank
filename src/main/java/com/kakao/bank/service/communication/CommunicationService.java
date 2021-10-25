package com.kakao.bank.service.communication;

import com.kakao.bank.domain.dto.account.request.GetAccountListDto;
import com.kakao.bank.domain.response.communication.GetAccountListRo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommunicationService {
    @Transactional(readOnly = true)
    List<GetAccountListRo> getMyAccounts(GetAccountListDto getAccountListDto);

    @Transactional(readOnly = true)
    String validAccount(String accountNumber);
}
