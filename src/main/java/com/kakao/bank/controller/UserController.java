package com.kakao.bank.controller;

import com.kakao.bank.domain.dto.user.request.SelfCertificationDto;
import com.kakao.bank.domain.dto.user.request.SimpleCertify;
import com.kakao.bank.domain.dto.user.request.UserProfileInfo;
import com.kakao.bank.domain.response.Response;
import com.kakao.bank.domain.response.ResponseData;
import com.kakao.bank.domain.response.user.SimpleUserInfoDto;
import com.kakao.bank.service.user.UserService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("프로필 사진 변경")
    @PatchMapping("/img")
    public Response patchProfileImg(MultipartFile file, HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        userService.changeProfileImg(file, userId);

        return new Response(HttpStatus.OK.value(), "프로필 사진 변경 성공");
    }

    /**
     * 간편 인증 번호 수정
     * @return httpCode, message
     */
    @ApiOperation("간편 인증 번호 수정")
    @PatchMapping("/certify")
    public Response patchSimpleCertifyNum(@RequestBody SimpleCertify simpleCertify
            , HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        userService.storeSimpleCertifyNumber(userId, simpleCertify.getNumber());

        return new Response(HttpStatus.CREATED.value(), "성공");
    }

    /**
     * 간편 인증
     * @return httpCode, message
     */
    @ApiOperation("간편 인증")
    @PostMapping("/certify")
    public Response simpleCertify(@RequestBody SimpleCertify simpleCertify, HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        userService.simpleCertify(userId, simpleCertify.getNumber());

        return new Response(HttpStatus.OK.value(), "성공");
    }

    /**
     * 유저 정보 수정
     * @return httpCode, message
     */
    @ApiOperation("유저 정보 수정")
    @PatchMapping("/profile")
    public Response changeProfile(@RequestBody UserProfileInfo userProfileInfo, HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        userService.changeProfile(userProfileInfo, userId);

        return new Response(HttpStatus.OK.value(), "성공");
    }

    @ApiOperation("본인인증")
    @PostMapping("/self/certification")
    public ResponseData<Boolean> selfCertification(@RequestBody SelfCertificationDto selfCertificationDto, HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        Boolean data = userService.selfCertification(selfCertificationDto, userId);

        return new ResponseData<>(HttpStatus.OK.value(), "성공", data);
    }

    /**
     * 토큰에서 유저 아이디 추출
     * @return userId
     */
    private String extractUserIdFromToken(HttpServletRequest request) {
        return (String) request.getAttribute("userId");
    }

    /**
     * 유저의 프로필 이미지와 이름 보기
     */
    @ApiOperation("유저의 프로필 이미지와 이름 보기")
    @GetMapping
    public ResponseData<SimpleUserInfoDto> getUserSimpleInfo(HttpServletRequest request) {
        String userId = extractUserIdFromToken(request);
        SimpleUserInfoDto data = userService.getUserSimpleInfo(userId);

        return new ResponseData<>(HttpStatus.OK.value(), "성공", data);
    }
}
