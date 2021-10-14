package com.kakao.bank.service.auth;

import com.kakao.bank.domain.dto.auth.request.RegisterReqDto;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    String login(String id, String password);

    Boolean validIdAndPassword(String id, String password);

    void register(RegisterReqDto registerReqDto, MultipartFile file);

    Boolean duplicateIdVerification(String id);

}
