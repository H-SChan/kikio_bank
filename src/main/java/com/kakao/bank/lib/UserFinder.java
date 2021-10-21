package com.kakao.bank.lib;

import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@RequiredArgsConstructor
@Component
public class UserFinder {
    private final UserRepo userRepo;

    public User getUser(String userId) {
        return userRepo.findById(userId).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "없는 유저")
        );
    }
}
