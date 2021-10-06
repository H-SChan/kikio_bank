package com.kakao.bank.service.user;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    @Transactional
    void changeProfileImg(MultipartFile file, String userId);

    @Transactional
    void storeSimpleCertifyNumber(String userId ,int simplePassword);

    @Transactional(readOnly = true)
    void simpleCertify(String userId, int password);
}
