package com.kakao.bank.service.communication;

import com.kakao.bank.domain.dto.account.request.GetAccountListDto;
import com.kakao.bank.domain.entity.Account;
import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.enums.Bank;
import com.kakao.bank.domain.repository.UserRepo;
import com.kakao.bank.domain.response.communication.GetAccountListRo;
import com.kakao.bank.lib.AccountFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunicationServiceImpl implements CommunicationService {

    private final AccountFinder accountFinder;

    private final UserRepo userRepo;

    @Value("${this.server.address}")
    private String thisAddress;

    @Value("${toss.server.address}")
    private String tossAddress;

    @Value("${daegu.server.address}")
    private String daeguAddress;

    @Value("${maagu.server.address}")
    private String maaguAddress;

    @Value("${kbank.server.address}")
    private String kbankAddress;

    /**
     * 사용자의 계좌 찾기
     * @return list < accountNumber, money, password, nickname >
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetAccountListRo> getMyAccounts(GetAccountListDto getAccountListDto) {
        User user = userRepo.findUserByPhoneNumber(getAccountListDto.getPhoneNumber()).orElseThrow(
                () -> new IllegalArgumentException("없는 사용자")
        );
        List<GetAccountListRo> result = new ArrayList<>();
        for (Account account : user.getAccounts()) {
            if (account.getBank() == Bank.KAKAO) {
                result.add(new GetAccountListRo(account));
            }
        }

        return result;
    }

    /**
     * 있는 계좌번호인지 확인
     * @return Name of the account holder
     */
    @Override
    @Transactional(readOnly = true)
    public String validAccount(String accountNumber) {
        return accountFinder.accountNumber(accountNumber).getUser().getName();
    }
}
