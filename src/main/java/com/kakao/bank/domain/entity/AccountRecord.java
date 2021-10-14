package com.kakao.bank.domain.entity;

import com.kakao.bank.domain.enums.Purpose;

import javax.persistence.*;
import java.util.Date;

@Entity
public class AccountRecord {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    // 일시
    @Column
    private Date date;

    // 돈
    @Column
    private Long money;

    // 입출금
    @Column
    private Purpose usingType;

    // 사용자 이름
    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
