package com.kakao.bank.service.user;

import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.repository.UserRepo;
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
        User user = getUser(userId);
        String fileName = fileService.storeFile(file);
        user.setProfileImg(serverAddress + "/file/" + fileName);
        userRepo.save(user);
    }

    private User getUser(String userId) {
        return userRepo.findById(userId).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "없는 유저")
        );
    }
}
