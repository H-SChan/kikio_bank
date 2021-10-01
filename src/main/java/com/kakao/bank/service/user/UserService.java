package com.kakao.bank.service.user;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    @Transactional
    void changeProfileImg(MultipartFile file, String userId);
}
