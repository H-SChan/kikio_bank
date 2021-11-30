package com.kakao.bank.service.account;

import com.kakao.bank.domain.dto.account.request.OpeningAccountDto;
import com.kakao.bank.domain.dto.account.request.RemittanceDto;
import com.kakao.bank.domain.dto.account.request.StoreAccountDto;
import com.kakao.bank.domain.dto.account.request.TakeMoneyDto;
import com.kakao.bank.domain.dto.communication.BroughtAccountDto;
import com.kakao.bank.domain.entity.Account;
import com.kakao.bank.domain.entity.AccountRecord;
import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.enums.AccountType;
import com.kakao.bank.domain.enums.Bank;
import com.kakao.bank.domain.enums.Purpose;
import com.kakao.bank.domain.repository.AccountRecordRepo;
import com.kakao.bank.domain.repository.AccountRepo;
import com.kakao.bank.domain.response.account.AccountRo;
import com.kakao.bank.domain.response.account.DetailAccountRo;
import com.kakao.bank.domain.response.record.RecordRo;
import com.kakao.bank.exception.CustomException;
import com.kakao.bank.lib.AccountFinder;
import com.kakao.bank.lib.UserFinder;
import com.kakao.bank.service.communication.CommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final UserFinder userFinder;
    private final AccountFinder accountFinder;

    private final AccountRepo accountRepo;
    private final AccountRecordRepo accountRecordRepo;

    private final CommunicationService communicationService;

    private final Random random = new Random();

    /**
     * 계좌 생성
     */
    @Override
    @Transactional
    public void openingAccount(OpeningAccountDto dto, String userId) {
        User user = userFinder.getUser(userId);
        StringBuilder accountNum = new StringBuilder(Bank.KAKAO.getBankNum() + dto.getAccountType().getIdentificationNum());
        do {
            for (int i = 0; i < 7; i++) {
                int randInt = random.nextInt(10);
                accountNum.append(randInt);
            }
        } while (Boolean.FALSE.equals(isThereAccountNum(accountNum.toString())));
        Account account = Account.builder()
                .accountNumber(accountNum.toString())
                .user(user)
                .bank(Bank.KAKAO)
                .money(10000L)
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .build();
        user.getAccounts().add(account);

        accountRepo.save(account);
    }

    /**
     * 계좌 리스트 받아오기
     *
     * @return accountNumber, nickname, money of accounts
     */
    @Override
    @Transactional(readOnly = true)
    public List<AccountRo> getAccounts(String userId) {
        User user = userFinder.getUser(userId);

        List<AccountRo> response = new ArrayList<>();
        for (Account account : user.getAccounts()) {
            AccountRo accountRo = new AccountRo();
            accountRo.accountToAccountRo(account, parseBank(account));
            response.add(accountRo);
        }

        return response;
    }

    /**
     * 계좌 내역 보기
     *
     * @return accountNumber, nickname, money, type, records
     */
    @Override
    @Transactional(readOnly = true)
    public DetailAccountRo getDetailAccounts(Long accountIdx) {
        Account account = getAccount(accountIdx);
        AccountType accountType;
        if (parseBank(account) == Bank.KAKAO) {
            accountType = parseAccountTypeKakao(account);
        } else throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
        DetailAccountRo detailAccountRo = new DetailAccountRo();
        detailAccountRo.accountToDetailAccountRo(account, accountType);

        for (AccountRecord accountRecord : account.getAccountRecords()) {
            RecordRo recordRo = new RecordRo();
            recordRo.accountRecordToRecordRo(accountRecord);
            detailAccountRo.getRecords().add(recordRo);
        }

        return detailAccountRo;
    }

    /**
     * 돈 가져오기
     */
    @Override
    @Transactional
    public void takeMoney(TakeMoneyDto takeMoneyDto, String userId) {
        User user = userFinder.getUser(userId);
        Account fromAccount = accountFinder.accountNumber(takeMoneyDto.getFromAccountNum());
        Account toAccount = accountFinder.accountNumber(takeMoneyDto.getToAccountNum());
        if (parseBank(fromAccount) != Bank.KAKAO && parseBank(toAccount) != Bank.KAKAO) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "해당 은행 기능 사용 불가");
        }
        // 진고
        long balance = fromAccount.getMoney() - takeMoneyDto.getMoney();
        if (balance < 0) {
            throw new CustomException(HttpStatus.FORBIDDEN, "잔액 부족");
        }

        saveAccountAndRecord(
                takeMoneyDto.getMoney(),
                Purpose.WITHDRAWAL,
                fromAccount,
                user,
                balance
        );

        saveAccountAndRecord(
                takeMoneyDto.getMoney(),
                Purpose.DEPOSIT,
                toAccount,
                user,
                toAccount.getMoney() + balance
        );
    }

    /**
     * 입출금을 할 때 계좌와 계좌 기록을 저장
     * @param usedMoney 사용 된 돈
     * @param usingType 입금 / 출금
     * @param account 저장 할 계좌
     * @param user 입금 - 돈을 보낸 / 출금 - 돈을 받은 사용자
     * @param balance 저장 될 계좌의 남은 잔고
     */
    @Override
    @Transactional
    public void saveAccountAndRecord(Long usedMoney, Purpose usingType, Account account, User user, Long balance) {
        List<AccountRecord> fromAccountRecords = account.getAccountRecords();
        AccountRecord accountRecord = AccountRecord.builder()
                .money(usedMoney)
                .account(account)
                .usingType(usingType)
                .name(user.getName())
                .build();
        accountRecordRepo.save(accountRecord);

        fromAccountRecords.add(accountRecord);
        Account accountSave = Account.builder()
                .idx(account.getIdx())
                .accountNumber(account.getAccountNumber())
                .accountRecords(fromAccountRecords)
                .money(balance)
                .bank(account.getBank())
                .password(account.getPassword())
                .user(user)
                .build();
        accountRepo.save(accountSave);
    }

    /**
     * 다른 은행의 내 계좌 불러오기
     *
     * @return accountNumber, nickname, money of accounts
     */
    @Override
    public List<AccountRo> getOtherBanksAccounts(String userId) {
        User user = userFinder.getUser(userId);
        List<AccountRo> list = new ArrayList<>();

        List<BroughtAccountDto> otherAccounts = communicationService.getOtherAccounts(user.getPhoneNumber());
        long idx = 0L;
        for (BroughtAccountDto otherAccount : otherAccounts) {
            AccountRo accountRo = new AccountRo();
            accountRo.broughtAccountToAccountRo(otherAccount, idx++);

            list.add(accountRo);
        }

        return list;
    }

    /**
     * 타 은행 계좌 저장하기
     */
    @Override
    @Transactional
    public void storeAccount(StoreAccountDto storeAccountDto, String userId) {
        User user = userFinder.getUser(userId);
        Account account = Account.builder()
                .accountNumber(storeAccountDto.getAccountNumber())
                .money(storeAccountDto.getMoney())
                .password(null)
                .bank(storeAccountDto.getBank())
                .nickname(storeAccountDto.getNickname())
                .user(user)
                .build();

        accountRepo.save(account);
    }

    /**
     * 은행 종류 받기
     */
    @Override
    public List<Bank> getKindOfBanks() {
        List<Bank> response = new ArrayList<>();
        response.add(Bank.KAKAO);
        response.add(Bank.DAEGU);
        response.add(Bank.MAAGU);
        response.add(Bank.TOSS);
        response.add(Bank.KBANK);

        return response;
    }

    /**
     * 송금
     */
    @Override
    @Transactional
    public void remittance(RemittanceDto remittanceDto, String userId) {
        Account account = accountFinder.accountNumber(remittanceDto.getFromAccountNumber());
        User user = userFinder.getUser(userId);
        // 수수료
        long fee;
        if (this.parseBank(remittanceDto.getFromAccountNumber()) == Bank.KAKAO) {
            fee = 0L;
        } else {
            fee = 500L;
        }
        // 잔액
        long balance = account.getMoney() - fee;
        if (balance < 0) {
            throw new CustomException(HttpStatus.FORBIDDEN, "잔액이 부족합니다.");
        }
        communicationService.remittance(remittanceDto);
        AccountRecord accountRecord = new AccountRecord(
                remittanceDto.getToMoney(),
                Purpose.WITHDRAWAL,
                account.getUser().getName(),
                account
        );
        account.getAccountRecords().add(accountRecord);
        accountRecordRepo.save(accountRecord);
        Account saveAccount = Account.builder()
                .idx(account.getIdx())
                .accountNumber(account.getAccountNumber())
                .money(balance)
                .password(account.getPassword())
                .bank(account.getBank())
                .nickname(account.getNickname())
                .user(user)
                .build();
        accountRepo.save(saveAccount);
    }


    private Account getAccount(Long accountIdx) {
        return accountRepo.getById(accountIdx);
    }

    public AccountType parseAccountTypeKakao(Account account) {
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
        } else throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
    }

    private Bank parseBank(Account account) {
        String bankNum = new String(
                account.getAccountNumber().getBytes(StandardCharsets.UTF_8),
                0,
                3);
        return getBank(bankNum);
    }

    private Bank parseBank(String accountNumber) {
        String bankNum = new String(
                accountNumber.getBytes(StandardCharsets.UTF_8),
                0,
                3);
        return getBank(bankNum);
    }

    private Bank getBank(String bankNum) {
        if (bankNum.equals(Bank.KAKAO.getBankNum())) {
            return Bank.KAKAO;
        } else if (bankNum.equals(Bank.DAEGU.getBankNum())) {
            return Bank.DAEGU;
        } else if (bankNum.equals(Bank.TOSS.getBankNum())) {
            return Bank.TOSS;
        } else if (bankNum.equals(Bank.MAAGU.getBankNum())) {
            return Bank.MAAGU;
        } else if (bankNum.equals(Bank.KBANK.getBankNum())) {
            return Bank.MAAGU;
        } else throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
    }

    private Boolean isThereAccountNum(String accountNumber) {
        return accountRepo.findAccountByAccountNumber(accountNumber).isEmpty();
    }
}
