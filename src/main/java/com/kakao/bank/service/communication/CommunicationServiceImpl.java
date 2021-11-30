package com.kakao.bank.service.communication;

import com.kakao.bank.domain.dto.account.request.RemittanceDto;
import com.kakao.bank.domain.dto.communication.BroughtAccountDto;
import com.kakao.bank.domain.dto.communication.CheckAccountPasswordDto;
import com.kakao.bank.domain.dto.communication.DepositDto;
import com.kakao.bank.domain.entity.Account;
import com.kakao.bank.domain.entity.AccountRecord;
import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.enums.Bank;
import com.kakao.bank.domain.enums.Purpose;
import com.kakao.bank.domain.repository.AccountRecordRepo;
import com.kakao.bank.domain.repository.AccountRepo;
import com.kakao.bank.domain.repository.UserRepo;
import com.kakao.bank.domain.response.communication.CheckAccountNumRo;
import com.kakao.bank.domain.response.communication.GetAccountListRo;
import com.kakao.bank.exception.CustomException;
import com.kakao.bank.lib.AccountFinder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommunicationServiceImpl implements CommunicationService {

    private final AccountFinder accountFinder;

    private final UserRepo userRepo;
    private final AccountRecordRepo accountRecordRepo;
    private final AccountRepo accountRepo;

    @Value("${this.server.address}")
    private String thisAddress;

    @Value("${toss.server.address}")
    private String tossAddress;

    @Value("${daegu.server.address}")
    private String daeguAddress;

    @Value("${maagu.server.address}")
    private String maaguAddress;

    @Value("${kbank.server.address}")
    private String kBankAddress;

    private final RestTemplate restTemplate = new RestTemplate();
    private final JSONParser jsonParser = new JSONParser();

    /**
     * 사용자의 계좌 찾기
     *
     * @return list < accountNumber, money, password, nickname >
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetAccountListRo> getMyAccounts(String phoneNumber) {
        User user = userRepo.findUserByPhoneNumber(phoneNumber).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자")
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
     *
     * @return Name of the account holder
     */
    @Override
    @Transactional(readOnly = true)
    public CheckAccountNumRo validAccount(String accountNumber) {
        return new CheckAccountNumRo("사용 가능", accountFinder.accountNumber(accountNumber));
    }

    /**
     * 다른 은행의 모든 계좌 가져오기
     */
    @Override
    public List<BroughtAccountDto> getOtherAccounts(String phoneNumber) {
        try {
            List<BroughtAccountDto> accounts = new ArrayList<>();

            List<BroughtAccountDto> maaguAccounts = getMaaguAccount(phoneNumber);
            List<BroughtAccountDto> tossAccounts = getTossAccount(phoneNumber);
            List<BroughtAccountDto> kBankAccounts = getKBankAccount(phoneNumber);

            accounts.addAll(maaguAccounts);
            accounts.addAll(tossAccounts);
            accounts.addAll(kBankAccounts);

            return accounts;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
        }
    }

    /**
     * 해당 계좌의 비밀번호가 맞는지 확인
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean checkPassword(CheckAccountPasswordDto dto) {
        Account account = accountFinder.accountNumber(dto.getAccountNum());
        return account.getPassword().equals(dto.getPassword());
    }

    /**
     * 송금
     */
    @Override
    @Transactional
    public void remittance(RemittanceDto remittanceDto) {
        try {
            // 다른 서버와 통신
            if (remittanceDto.getToBank() == Bank.TOSS) {
                this.remittanceToToss(remittanceDto);
            } else if (remittanceDto.getToBank() == Bank.MAAGU) {
                this.remittanceToMaagu(remittanceDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
        }
    }

    /**
     * 입금
     */
    @Override
    @Transactional
    public void deposit(DepositDto depositDto) {
        Account account = accountFinder.accountNumber(depositDto.getAccountNumber());
        AccountRecord accountRecord = new AccountRecord(
                depositDto.getMoney(),
                Purpose.DEPOSIT,
                depositDto.getName(),
                account
        );
        accountRecordRepo.save(accountRecord);
        Account saveAccount = Account.builder()
                .idx(account.getIdx())
                .accountNumber(account.getAccountNumber())
                .money(account.getMoney() + depositDto.getMoney())
                .password(account.getPassword())
                .bank(account.getBank())
                .nickname(account.getNickname())
                .user(account.getUser())
                .accountRecords(account.getAccountRecords())
                .build();
        saveAccount.getAccountRecords().add(accountRecord);
        accountRepo.save(saveAccount);
    }

    public List<BroughtAccountDto> getMaaguAccount(String phoneNumber) throws ParseException {
        try {
            List<BroughtAccountDto> list = new ArrayList<>();

            String url = maaguAddress + "/account/find/phone/" + phoneNumber;
            String res = restTemplate.getForObject(url, String.class);

            JSONObject jsonObj = (JSONObject) jsonParser.parse(res);
            JSONArray data = (JSONArray) jsonObj.get("data");
//            JSONArray accountDatum = (JSONArray) data.get("account");

            for (Object o : data) {
                JSONObject accountData = (JSONObject) o;

                String accountNum = (String) accountData.get("accountNum");
                String nickName = (String) accountData.get("name");
                String password = (String) accountData.get("password");
                Long money = (Long) accountData.get("pay");
                BroughtAccountDto account = new BroughtAccountDto(
                        accountNum,
                        nickName,
                        money,
                        password,
                        Bank.MAAGU
                );
                list.add(account);
            }

            return list;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<BroughtAccountDto> getTossAccount(String phoneNumber) throws ParseException {
        try {
            List<BroughtAccountDto> list = new ArrayList<>();

            String url = tossAddress + "/account/" + phoneNumber;
            String res = restTemplate.getForObject(url, String.class);

            JSONArray jsonObj = (JSONArray) jsonParser.parse(res);

            for (Object o : jsonObj) {
                JSONObject accountData = (JSONObject) o;

                String name = (String) accountData.get("name");
                Long money = (Long) accountData.get("money");
                String accountNumber = accountData.get("accountNumber").toString();

                BroughtAccountDto account = new BroughtAccountDto(accountNumber, name, money, null, Bank.TOSS);
                list.add(account);
            }
            return list;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<BroughtAccountDto> getKBankAccount(String phoneNumber) throws ParseException {
        try {
            List<BroughtAccountDto> list = new ArrayList<>();

            String url = kBankAddress + "/api/open/accounts/" + phoneNumber;
            String res = restTemplate.getForObject(url, String.class);

            JSONObject jsonObj = (JSONObject) jsonParser.parse(res);
            JSONArray jsonArray = (JSONArray) jsonObj.get("accounts");

            for (Object o : jsonArray) {
                JSONObject accountData = (JSONObject) o;

                String accountNumber = (String) accountData.get("id");
                JSONObject accountNickname = (JSONObject) accountData.get("account_nickname");
                String name = (String) accountNickname.get("String");
                Long money = (Long) accountData.get("balance");
                String password = (String) accountData.get("password");

                BroughtAccountDto account = new BroughtAccountDto(
                        accountNumber,
                        name,
                        money,
                        password,
                        Bank.KBANK
                );
                list.add(account);
            }
            return list;
        } catch (ResourceAccessException e) {
            log.warn(e.getLocalizedMessage());
        }
        return Collections.emptyList();
    }

    private void remittanceToToss(RemittanceDto remittanceDto) {
        try {
            String url = tossAddress + "/account/receive";
            @Getter
            @Setter
            class TossRemittanceDto {
                private String sendAccountNumber;
                private String receiveAccountNumber;
                private Long money;

                public TossRemittanceDto(RemittanceDto dto) {
                    this.sendAccountNumber = dto.getFromAccountNumber();
                    this.receiveAccountNumber = dto.getToAccountNumber();
                    this.money = dto.getToMoney();
                }
            }
            String res = restTemplate.postForObject(url, new TossRemittanceDto(remittanceDto), String.class);
            log.info(res);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            log.error(e.getMessage());
        }
    }

    private void remittanceToMaagu(RemittanceDto remittanceDto) {
        try {
            String url = maaguAddress + "/transaction/receive";
            @Getter
            @Setter
            class MaaguRemittanceDto {
                private String sendAccountNum;
                private String receiveAccountNum;
                private Long receivePay;

                public MaaguRemittanceDto(RemittanceDto dto) {
                    this.sendAccountNum = dto.getFromAccountNumber();
                    this.receiveAccountNum = dto.getToAccountNumber();
                    this.receivePay = dto.getToMoney();
                }
            }
            String res = restTemplate.postForObject(url, new MaaguRemittanceDto(remittanceDto), String.class);
            log.info(res);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            log.error(e.getMessage());
        }
    }
}
