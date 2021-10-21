package com.kakao.bank.service.auth;

import com.kakao.bank.domain.dto.auth.request.RegisterReqDto;
import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.repository.UserRepo;
import com.kakao.bank.lib.UserFinder;
import com.kakao.bank.service.file.FileService;
import com.kakao.bank.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{

    private final UserFinder userFinder;

    private final UserRepo userRepo;

    private final JwtService jwtService;
    private final FileService fileService;

    @Value("${this.server.address}")
    private String serverAddress;

    /**
     * 로그인
     * @return token
     */
    @Override
    @Transactional(readOnly = true)
    public String login(String id, String password) {
        if (Boolean.TRUE.equals(validIdAndPassword(id, password))) {
            return jwtService.createToken(id);
        } else {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "잘못된 비밀번호");
        }
    }

    /**
     * 아이디, 비밀번호 확인
     * @return {@code ture} / {@code false}
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean validIdAndPassword(String id, String password) {
        User user = userFinder.getUser(id);
        return user.getPassword().equals(password);
    }

    /**
     * 회원가입
     */
    @Override
    @Transactional
    public void register(RegisterReqDto registerReqDto, MultipartFile file) {
        String fileName;
        if (file.isEmpty()) {
            fileName = "default.png";
        } else {
            fileName = fileService.storeFile(file);
        }
        userRepo.save(registerReqDto.toEntity(serverAddress + "/file/" + fileName));
    }

    /**
     * 아이디 중복확인
     * @return {@code true} / {@code false}
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean duplicateIdVerification(String id) {
        return userRepo.findById(id).isEmpty();
    }

}
