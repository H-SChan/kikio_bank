package com.kakao.bank.service.communication;

import com.kakao.bank.domain.dto.communication.BroughtAccountDto;
import com.kakao.bank.domain.entity.Account;
import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.enums.Bank;
import com.kakao.bank.domain.repository.UserRepo;
import com.kakao.bank.domain.response.communication.CheckAccountNumRo;
import com.kakao.bank.domain.response.communication.GetAccountListRo;
import com.kakao.bank.exception.CustomException;
import com.kakao.bank.lib.AccountFinder;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
    private String kBankAddress;

    private final RestTemplate restTemplate = new RestTemplate();
    private final JSONParser jsonParser = new JSONParser();

    /**
     * 사용자의 계좌 찾기
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

            accounts.addAll(maaguAccounts);
            accounts.addAll(tossAccounts);

            return accounts;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
        }
    }

    public List<BroughtAccountDto> getMaaguAccount(String phoneNumber) throws ParseException {
        List<BroughtAccountDto> list = new ArrayList<>();

        String url = maaguAddress + "/account/find/" + phoneNumber;
        String res = restTemplate.getForObject(url, String.class);

        JSONObject jsonObj = (JSONObject) jsonParser.parse(res);
        JSONObject data = (JSONObject) jsonObj.get("data");
        JSONArray accountDatum = (JSONArray) data.get("account");

        for (Object o : accountDatum) {
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
    }

    public List<BroughtAccountDto> getTossAccount(String phoneNumber) throws ParseException {
        List<BroughtAccountDto> list = new ArrayList<>();

        String url = tossAddress + "/account/" + phoneNumber;
        String res = restTemplate.getForObject(url, String.class);

        JSONArray jsonObj = (JSONArray) jsonParser.parse(res);

        for (Object o : jsonObj) {
            JSONObject accountData = (JSONObject) o;

            String name = (String) accountData.get("name");
            Long money = (Long) accountData.get("money");
            String accountNumber = (String) accountData.get("accountNumber").toString();

            BroughtAccountDto account = new BroughtAccountDto(accountNumber, name, money, null, Bank.TOSS);
            list.add(account);
        }
        return list;
    }
}
