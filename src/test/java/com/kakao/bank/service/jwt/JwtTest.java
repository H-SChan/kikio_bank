package com.kakao.bank.service.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtTest {
    @Autowired
    private JwtService jwtService;

    /**
     * 토큰 생성
     */
    @Test
    void createToken() {
        String id = "qewr1234";

        String token = jwtService.createToken(id);

        System.out.println(token);

        assertThat(token).isNotEmpty();
    }
}
