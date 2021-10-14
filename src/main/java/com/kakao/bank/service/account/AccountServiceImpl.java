package com.kakao.bank.service.account;

import com.kakao.bank.domain.dto.account.request.OpeningAccountDto;
import com.kakao.bank.domain.entity.Account;
import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.enums.Bank;
import com.kakao.bank.domain.repository.AccountRepo;
import com.kakao.bank.domain.repository.UserRepo;
import com.kakao.bank.domain.response.account.AccountRo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService{

    private final AccountRepo accountRepo;
    private final UserRepo userRepo;

    private final String BANK_NUM = "108";
    private Random random = new Random();

    /**
     * 계좌 생성
     */
    @Override
    @Transactional
    public void openingAccount(OpeningAccountDto dto, String userId) {
        User user = getUser(userId);
        StringBuilder accountNum = new StringBuilder(BANK_NUM + dto.getAccountType().getIdentificationNum());
        for (int i = 0; i < 7; i++) {
            int randInt = random.nextInt(10);
            System.out.println(randInt);
            accountNum.append(randInt);
        }
        Account account = Account.builder()
                .accountNumber(accountNum.toString())
                .user(user)
                .bank(Bank.SAECHAN)
                .money(10000L)
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .build();
        user.getAccounts().add(account);

        accountRepo.save(account);
    }

    /**
     * 계좌 리스트 받아오기
     * @return accountNumber, nickname, money of accounts
     */
    @Override
    @Transactional(readOnly = true)
    public List<AccountRo> getAccounts(String userId) {
        User user = getUser(userId);

        List<AccountRo> response = new ArrayList<>();
        for (Account account : user.getAccounts()) {
            AccountRo accountRo = new AccountRo();
            accountRo.accountToAccountRo(account);
            response.add(accountRo);
        }

        return response;
    }

    private User getUser(String userId) {
        return userRepo.findById(userId).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "없는 유저")
        );
    }
}
