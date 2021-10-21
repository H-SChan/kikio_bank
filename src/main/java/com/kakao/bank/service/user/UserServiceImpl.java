package com.kakao.bank.service.user;

import com.kakao.bank.domain.dto.user.request.UserProfileInfo;
import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.repository.UserRepo;
import com.kakao.bank.lib.UserFinder;
import com.kakao.bank.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserFinder userFinder;

    private final FileService fileService;

    private final UserRepo userRepo;

    @Value("${this.server.address}")
    private String serverAddress;

    /**
     * 프로필 사진 변경
     */
    @Override
    @Transactional
    public void changeProfileImg(MultipartFile file, String userId) {
        User user = userFinder.getUser(userId);
        String fileName = fileService.storeFile(file);
        user.setProfileImg(serverAddress + "/file/" + fileName);
        userRepo.save(user);
    }

    /**
     * 간편 인증 비밀번호 수정
     */
    @Override
    @Transactional
    public void storeSimpleCertifyNumber(String userId, int number) {
        User user = userFinder.getUser(userId);
        user.setSimpleNumber(number);
        userRepo.save(user);
    }

    /**
     * 간편 인증
     */
    @Override
    @Transactional(readOnly = true)
    public void simpleCertify(String userId, int password) {
        User user = userFinder.getUser(userId);
        if (!user.getSimpleNumber().equals(password)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "비밀번호 틀림");
        }
    }

    /**
     * 유저 정보 수정
     */
    @Override
    @Transactional
    public void changeProfile(UserProfileInfo userProfileInfo, String userId) {
        User user = userFinder.getUser(userId);
        if (userProfileInfo.getName() == null) {
            userProfileInfo.setName(user.getName());
        }
        if (userProfileInfo.getRRM() == null) {
            userProfileInfo.setRRM(user.getResidentRegistrationNumber());
        }
        if (userProfileInfo.getPhoneNumber() == null) {
            userProfileInfo.setPhoneNumber(user.getPhoneNumber());
        }
        if (userProfileInfo.getNickname() == null) {
            userProfileInfo.setNickname(user.getNickname());
        }
        userRepo.save(userProfileInfo.toEntity(user));
    }
}
