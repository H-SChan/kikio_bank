package com.kakao.bank.domain.response.user;

import com.kakao.bank.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SimpleUserInfoDto {
    private String name;
    private String profileImg;

    public SimpleUserInfoDto(User user) {
        this.name = user.getName();
        this.profileImg = user.getProfileImg();
    }
}
