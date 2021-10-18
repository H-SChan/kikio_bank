package com.kakao.bank.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    // 아이디
    @Column(unique = true)
    private String id;

    // 비밀번호
    @Column
    private String password;

    // 전화번호
    @Column
    private String phoneNumber;

    // 주민등록번호
    @Column
    private String residentRegistrationNumber;

    // 이름
    @Column
    private String name;

    // 별명
    @Column
    private String nickname;

    // 프로필 사진
    @Column
    private String profileImg;

    // 간편 인증 번호
    @Column
    private Integer simpleNumber;

    @OneToMany(mappedBy = "user")
    private List<Account> accounts;

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setSimpleNumber(Integer simpleNumber) {
        this.simpleNumber = simpleNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setResidentRegistrationNumber(String residentRegistrationNumber) {
        this.residentRegistrationNumber = residentRegistrationNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
