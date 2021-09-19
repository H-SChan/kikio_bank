package com.kakao.bank.domain.entity;

import javax.persistence.*;

@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    // 아이디
    @Column
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
}
