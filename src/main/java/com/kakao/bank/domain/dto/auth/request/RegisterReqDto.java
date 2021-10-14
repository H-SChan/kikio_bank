package com.kakao.bank.domain.dto.auth.request;

import com.kakao.bank.domain.entity.User;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter
public class RegisterReqDto {
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영어와 숫자만 입력해주세요.")
    @Min(value = 5, message = "id는 5글자 이상 입력해야합니다.")
    private String id;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)$", message = "영어, 숫자, 특수문자를 각각 한번 이상 입력해주세요")
    @Size(min = 8, max = 16, message = "비밀번호는 8~16글자 사이로 입력해주세요.")
    private String password;

    private String phoneNumber;

    private String residentRegistrationNumber;

    private String name;

    private String nickname;

    public User toEntity() {
        return User.builder()
                .id(id)
                .password(password)
                .phoneNumber(phoneNumber)
                .residentRegistrationNumber(residentRegistrationNumber)
                .name(name)
                .nickname(nickname)
                .build();
    }
}
