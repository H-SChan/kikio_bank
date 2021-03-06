package com.kakao.bank.controller;

import com.kakao.bank.domain.dto.account.request.GetAccountListDto;
import com.kakao.bank.domain.dto.communication.CheckAccountPasswordDto;
import com.kakao.bank.domain.dto.communication.DepositDto;
import com.kakao.bank.domain.response.Response;
import com.kakao.bank.domain.response.communication.CheckAccountNumRo;
import com.kakao.bank.domain.response.communication.GetAccountListRo;
import com.kakao.bank.service.communication.CommunicationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/communication")
@RestController
public class CommunicationController {

    private final CommunicationService communicationService;

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

    @ApiOperation("사용자 계좌 찾기")
    @GetMapping("/{phoneNumber}")
    public List<GetAccountListRo> getAccounts(@PathVariable String phoneNumber) {
        return communicationService.getMyAccounts(phoneNumber);
    }

    @ApiOperation("계좌번호 확인")
    @GetMapping("check/accountNum/{accountNumber}")
    public CheckAccountNumRo validAccountNumber(@PathVariable String accountNumber) {
        return communicationService.validAccount(accountNumber);
    }

    @ApiOperation("계좌의 비밀번호가 맞는지 확인")
    @GetMapping("/check/accountPw")
    public Boolean checkAccountPassword(@RequestBody CheckAccountPasswordDto checkAccountPasswordDto) {
        return communicationService.checkPassword(checkAccountPasswordDto);
    }

    @ApiOperation("입금")
    @PatchMapping("/deposit")
    public Response deposit(@RequestBody DepositDto depositDto) {
        communicationService.deposit(depositDto);

        return new Response(HttpStatus.OK.value(), "성공");
    }
}
