package com.kakao.bank.service.account;

import com.kakao.bank.domain.dto.account.request.OpeningAccountDto;
import com.kakao.bank.domain.dto.account.request.TakeMoneyDto;
import com.kakao.bank.domain.entity.Account;
import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.enums.Purpose;
import com.kakao.bank.domain.response.account.AccountRo;
import com.kakao.bank.domain.response.account.DetailAccountRo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountService {
    void openingAccount(OpeningAccountDto dto, String userId);

    @Transactional(readOnly = true)
    List<AccountRo> getAccounts(String userId);

    @Transactional(readOnly = true)
    DetailAccountRo getDetailAccounts(Long accountIdx);

    @Transactional
    void takeMoney(TakeMoneyDto takeMoneyDto, String userId);

    @Transactional
    void saveAccountAndRecord(Long usedMoney, Purpose usingType, Account account, User user, Long balance);

    List<AccountRo> getOtherBanksAccounts(String userId);
}
