package com.kakao.bank.controller;

import com.kakao.bank.domain.dto.account.request.OpeningAccountDto;
import com.kakao.bank.domain.dto.account.request.TakeMoneyDto;
import com.kakao.bank.domain.response.Response;
import com.kakao.bank.domain.response.ResponseData;
import com.kakao.bank.domain.response.account.AccountRo;
import com.kakao.bank.domain.response.account.DetailAccountRo;
import com.kakao.bank.service.account.AccountService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/account")
@RestController
public class AccountController {

    private final AccountService accountService;

    @ApiOperation("계좌 개설")
    @PostMapping
    public Response openingAccount(
            @RequestBody @Valid OpeningAccountDto accountDto,
            HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        accountService.openingAccount(accountDto, userId);

        return new Response(HttpStatus.OK.value(), "성공");
    }

    @ApiOperation("계좌 목록 받기")
    @GetMapping
    public ResponseData<List<AccountRo>> getAccountList(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        List<AccountRo> data = accountService.getAccounts(userId);

        return new ResponseData<>(HttpStatus.OK.value(), "성공", data);
    }

    @ApiOperation("계좌 내역 보기")
    @GetMapping("/detail/{accountId}")
    public ResponseData<DetailAccountRo> getDetailAccount(@PathVariable Long accountId) {
        DetailAccountRo data = accountService.getDetailAccounts(accountId);

        return new ResponseData<>(HttpStatus.OK.value(), "성공", data);
    }

    @ApiOperation(value = "내 계좌에서 돈 가져오기", notes = "내 은행의 계좌만 가능")
    @PostMapping("/take")
    public Response takeMoney(@RequestBody TakeMoneyDto takeMoneyDto, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        accountService.takeMoney(takeMoneyDto, userId);

        return new Response(HttpStatus.OK.value(), "성공");
    }

    @ApiOperation("타 은행의 내 계좌 보기")
    @GetMapping("/other")
    public ResponseData<List<AccountRo>> getOtherBanksAccounts(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        List<AccountRo> data = accountService.getOtherBanksAccounts(userId);

        return new ResponseData<>(HttpStatus.OK.value(), "성공", data);
    }

}
