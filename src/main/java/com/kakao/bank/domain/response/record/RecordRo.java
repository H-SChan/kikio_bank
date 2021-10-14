package com.kakao.bank.domain.response.record;

import com.kakao.bank.domain.entity.AccountRecord;
import com.kakao.bank.domain.enums.Purpose;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class RecordRo {

    private Date date;
    private Long usedMoney;
    private Purpose howUsed;
    private String name;

    public void accountRecordToRecordRo(AccountRecord accountRecord) {
        this.date = accountRecord.getDate();
        this.usedMoney = accountRecord.getMoney();
        this.howUsed = accountRecord.getUsingType();
        this.name = accountRecord.getName();
    }
}
