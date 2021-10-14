package com.kakao.bank.controller;

import com.kakao.bank.domain.dto.account.request.OpeningAccountDto;
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

}
