package com.kakao.bank.domain.entity;

import com.kakao.bank.domain.enums.Bank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    // 계좌번호
    @Column
    private String accountNumber;

    // 돈
    @Column
    private Long money;

    // 계좌 비밀번호
    @Column
    private String password;

    // 은행 종류
    @Column
    private Bank bank;

    // 계좌 이름
    @Column
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account")
    private List<AccountRecord> accountRecords;
}
