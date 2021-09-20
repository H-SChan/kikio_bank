package com.kakao.bank.service.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtTest {
    @Autowired
    private JwtService jwtService;

    @Test
    void createToken() {
        String id = "qewr1234";

        String token = jwtService.createToken(id);

        System.out.println(token);
    }
}
