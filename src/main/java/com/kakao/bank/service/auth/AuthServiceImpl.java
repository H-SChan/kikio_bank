package com.kakao.bank.service.auth;

import com.kakao.bank.domain.dto.auth.request.RegisterReqDto;
import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.repository.UserRepo;
import com.kakao.bank.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepo userRepo;
    private final JwtService jwtService;

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
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "잘못된 비밀번호");
        }
    }

    /**
     * 아이디, 비밀번호 확인
     * @return {@code ture} / {@code false}
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean validIdAndPassword(String id, String password) {
        User user = userRepo.findById(id).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없음")
        );
        return user.getPassword().equals(password);
    }

    /**
     * 회원가입
     */
    @Override
    @Transactional(readOnly = true)
    public void register(RegisterReqDto registerReqDto) {
        userRepo.save(registerReqDto.toEntity());
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
