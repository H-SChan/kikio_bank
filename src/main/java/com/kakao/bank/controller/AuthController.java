package com.kakao.bank.controller;

import com.kakao.bank.domain.dto.auth.request.LoginReq;
import com.kakao.bank.domain.dto.auth.request.RegisterReqDto;
import com.kakao.bank.domain.dto.auth.response.LoginRo;
import com.kakao.bank.domain.response.Response;
import com.kakao.bank.domain.response.ResponseData;
import com.kakao.bank.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Response register(@RequestBody RegisterReqDto registerReqDto) {
        authService.register(registerReqDto);

        return new Response(HttpStatus.OK, "회원가입 완료");
    }

    @PostMapping("/available/id")
    public ResponseData<Map<String, Boolean>> availableId(String id) {
        Map<String, Boolean> data = new HashMap<>();
        if (authService.duplicateIdVerification(id)) {
            data.put("available", true);

            return new ResponseData<>(HttpStatus.OK, "사용 가능", data);
        } else {
            data.put("available", false);

            return new ResponseData<>(HttpStatus.OK, "사용 불가", data);
        }
    }

    @PostMapping("/login")
    public ResponseData<LoginRo> login(@RequestBody LoginReq loginReq) {
        String token = authService.login(loginReq.getId(), loginReq.getPassword());
        LoginRo data = new LoginRo(token);

        return new ResponseData<>(HttpStatus.OK, "로그인 성공", data);
    }
}
