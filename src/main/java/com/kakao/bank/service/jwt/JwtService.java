package com.kakao.bank.service.jwt;

import com.kakao.bank.domain.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface JwtService {
    String createToken(String id);

    @Transactional(readOnly = true)
    String validToken(String token);
}
