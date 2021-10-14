package com.kakao.bank.controller;

import com.kakao.bank.domain.dto.account.request.OpeningAccountDto;
import com.kakao.bank.domain.response.Response;
import com.kakao.bank.service.account.AccountService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

}
