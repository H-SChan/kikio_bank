package com.kakao.bank.domain.dto.user.request;

import com.kakao.bank.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserProfileInfo {
    private String phoneNumber;
    private String rRM;
    private String name;
    private String nickname;

    public User toEntity(User user) {
        user.setPhoneNumber(phoneNumber);
        user.setResidentRegistrationNumber(rRM);
        user.setName(name);
        user.setNickname(nickname);

        return user;
    }
}
