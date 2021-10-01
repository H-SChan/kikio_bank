package com.kakao.bank.controller;

import com.kakao.bank.domain.response.Response;
import com.kakao.bank.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    @PatchMapping("/img")
    public Response patchProfileImg(MultipartFile file, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        userService.changeProfileImg(file, userId);

        return new Response(HttpStatus.OK, "프로필 사진 변경 성공");
    }
}
