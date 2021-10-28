package com.kakao.bank.domain.repository;

import com.kakao.bank.domain.entity.AccountRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRecordRepo extends JpaRepository<AccountRecord, Long> {
}
