package com.kakao.bank.domain.entity;

import com.kakao.bank.domain.enums.Purpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class AccountRecord {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    // 일시
    @CreatedDate
    @Column
    private LocalDateTime date;

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
