package com.kakao.bank.controller;

import com.kakao.bank.domain.dto.user.request.SimpleCertify;
import com.kakao.bank.domain.response.Response;
import com.kakao.bank.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 프로필 사진 변경
     * @return httpCode, message
     */
    @PatchMapping("/img")
    public Response patchProfileImg(MultipartFile file, HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        userService.changeProfileImg(file, userId);

        return new Response(HttpStatus.OK, "프로필 사진 변경 성공");
    }

    /**
     * 간편 인증 번호 수정
     * @return httpCode, message
     */
    @PatchMapping("/certify")
    public Response patchSimpleCertifyNum(@RequestBody SimpleCertify simpleCertify
            , HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        userService.storeSimpleCertifyNumber(userId, simpleCertify.getNumber());

        return new Response(HttpStatus.OK, "성공");
    }

    /**
     * 간편 인증
     * @return httpCode, message
     */
    @PostMapping("/certify")
    public Response simpleCertify(@RequestBody SimpleCertify simpleCertify, HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        userService.simpleCertify(userId, simpleCertify.getNumber());

        return new Response(HttpStatus.OK, "성공");
    }

    /**
     * 토큰에서 유저 아이디 추출
     * @return userId
     */
    private String extractUserIdFromToken(HttpServletRequest request) {
        return (String) request.getAttribute("userId");
    }
}
