package com.kakao.bank.service.auth;

import com.kakao.bank.domain.dto.auth.request.RegisterReqDto;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {
    String login(String id, String password);

    Boolean validIdAndPassword(String id, String password);

    void register(RegisterReqDto registerReqDto);

    Boolean duplicateIdVerification(String id);

    @Transactional
    void storeSimpleLoginPassword(int simplePassword);
}
