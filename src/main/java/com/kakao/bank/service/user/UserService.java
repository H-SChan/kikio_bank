package com.kakao.bank.service.user;

import com.kakao.bank.domain.dto.user.request.SelfCertificationDto;
import com.kakao.bank.domain.dto.user.request.UserProfileInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    @Transactional
    void changeProfileImg(MultipartFile file, String userId);

    @Transactional
    void storeSimpleCertifyNumber(String userId ,int simplePassword);

    @Transactional(readOnly = true)
    void simpleCertify(String userId, int password);

    @Transactional
    void changeProfile(UserProfileInfo userProfileInfo, String userId);

    @Transactional(readOnly = true)
    Boolean selfCertification(SelfCertificationDto selfCertificationDto, String userId);
}
