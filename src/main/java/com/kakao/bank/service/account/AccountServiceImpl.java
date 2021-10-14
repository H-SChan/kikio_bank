package com.kakao.bank.service.account;

import com.kakao.bank.domain.dto.account.request.OpeningAccountDto;
import com.kakao.bank.domain.entity.Account;
import com.kakao.bank.domain.entity.AccountRecord;
import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.enums.AccountType;
import com.kakao.bank.domain.enums.Bank;
import com.kakao.bank.domain.repository.AccountRepo;
import com.kakao.bank.domain.repository.UserRepo;
import com.kakao.bank.domain.response.account.AccountRo;
import com.kakao.bank.domain.response.account.DetailAccountRo;
import com.kakao.bank.domain.response.record.RecordRo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.charset.StandardCharsets;
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
            accountRo.accountToAccountRo(account, parseBank(account));
            response.add(accountRo);
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public DetailAccountRo getDetailAccounts(Long accountIdx) {
        Account account = getAccount(accountIdx);
        AccountType accountType;
        if (parseBank(account) == Bank.SAECHAN) {
            accountType = parseAccountTypeSaeChan(account);
        }
        else throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
        DetailAccountRo detailAccountRo = new DetailAccountRo();
        detailAccountRo.accountToDetailAccountRo(account, accountType);

        for (AccountRecord accountRecord : account.getAccountRecords()) {
            RecordRo recordRo = new RecordRo();
            recordRo.accountRecordToRecordRo(accountRecord);
            detailAccountRo.getRecords().add(recordRo);
        }

        return detailAccountRo;
    }

    private User getUser(String userId) {
        return userRepo.findById(userId).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "없는 유저")
        );
    }

    private Account getAccount(Long accountIdx) {
        return accountRepo.getById(accountIdx);
    }

    public AccountType parseAccountTypeSaeChan(Account account) {
        String identificationNum = new String(
                account.getAccountNumber().getBytes(StandardCharsets.UTF_8),
                3,
                2);
        if (identificationNum.equals(AccountType.BASIC.getIdentificationNum())) {
            return AccountType.BASIC;
        } else if (identificationNum.equals(AccountType.DEPOSIT.getIdentificationNum())) {
            return AccountType.DEPOSIT;
        } else if (identificationNum.equals(AccountType.INSTALLMENT_SAVING.getIdentificationNum())) {
            return AccountType.INSTALLMENT_SAVING;
        } else throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }

    private Bank parseBank(Account account) {
        String bankNum = new String(
                account.getAccountNumber().getBytes(StandardCharsets.UTF_8),
                0,
                3);
        if(bankNum.equals(Bank.SAECHAN.getBankNum())) {
            return Bank.SAECHAN;
        } else throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }
}
