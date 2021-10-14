package com.kakao.bank.controller;

import com.kakao.bank.domain.dto.auth.request.LoginReq;
import com.kakao.bank.domain.dto.auth.request.RegisterReqDto;
import com.kakao.bank.domain.response.auth.AvailableIdRo;
import com.kakao.bank.domain.response.auth.LoginRo;
import com.kakao.bank.domain.response.Response;
import com.kakao.bank.domain.response.ResponseData;
import com.kakao.bank.service.auth.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     * @return httpCode, message
     */
    @ApiOperation("회원가입")
    @PostMapping("/register")
    public Response register(@ModelAttribute @Valid RegisterReqDto registerReqDto, MultipartFile file) {
        if (file == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "파일이 널이다");
        }
        authService.register(registerReqDto, file);

        return new Response(HttpStatus.OK.value(), "회원가입 완료");
    }

    /**
     * 아이디 중복 확인
     * @return httpCode, message, boolean
     */
    @ApiOperation("아이디 중복 확인")
    @PostMapping("/available/id")
    public ResponseData<AvailableIdRo> availableId(@RequestBody String id) {
        AvailableIdRo data;
        if (Boolean.TRUE.equals(authService.duplicateIdVerification(id))) {
            data = new AvailableIdRo(true);

            return new ResponseData<>(HttpStatus.OK.value(), "사용 가능", data);
        } else {
            data = new AvailableIdRo(false);

            return new ResponseData<>(HttpStatus.OK.value(), "사용 불가", data);
        }
    }

    /**
     * 로그인
     * @return httpCode, message, token
     */
    @ApiOperation("로그인")
    @PostMapping("/login")
    public ResponseData<LoginRo> login(@RequestBody LoginReq loginReq) {
        String token = authService.login(loginReq.getId(), loginReq.getPassword());
        LoginRo data = new LoginRo(token);

        return new ResponseData<>(HttpStatus.OK.value(), "로그인 성공", data);
    }

}
