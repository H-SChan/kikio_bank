package com.kakao.bank.controller;

import com.kakao.bank.domain.dto.auth.request.LoginReq;
import com.kakao.bank.domain.dto.auth.request.RegisterReqDto;
import com.kakao.bank.domain.dto.auth.request.SimplePassword;
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

    /**
     * 회원가입
     * @return httpCode, message
     */
    @PostMapping("/register")
    public Response register(@RequestBody RegisterReqDto registerReqDto) {
        authService.register(registerReqDto);

        return new Response(HttpStatus.OK, "회원가입 완료");
    }

    /**
     * 아이디 중복 확인
     * @return httpCode, message, boolean
     */
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

    /**
     * 로그인
     * @return httpCode, message, token
     */
    @PostMapping("/login")
    public ResponseData<LoginRo> login(@RequestBody LoginReq loginReq) {
        String token = authService.login(loginReq.getId(), loginReq.getPassword());
        LoginRo data = new LoginRo(token);

        return new ResponseData<>(HttpStatus.OK, "로그인 성공", data);
    }

    /**
     * 간편 로그인 비밀번호 설정
     * @return httpCode, message
     */
    @PostMapping("/simple/pw")
    public Response storeSimpleLoginPassword(@RequestBody SimplePassword simplePassword) {
        authService.storeSimpleLoginPassword(simplePassword.getPassword());

        return new Response(HttpStatus.OK, "성공");
    }
}
