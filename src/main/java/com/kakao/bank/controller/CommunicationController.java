package com.kakao.bank.controller;

import com.kakao.bank.domain.dto.account.request.GetAccountListDto;
import com.kakao.bank.domain.response.communication.GetAccountListRo;
import com.kakao.bank.service.communication.CommunicationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @GetMapping()
    public List<GetAccountListRo> getAccounts(@RequestBody GetAccountListDto getAccountListDto) {
        return communicationService.getMyAccounts(getAccountListDto);
    }

    @ApiOperation("계좌번호 확인")
    @GetMapping("check/accountNum/{accountNumber}")
    public String validAccountNumber(@PathVariable String accountNumber) {
        return communicationService.validAccount(accountNumber);
    }
}
