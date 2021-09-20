package com.kakao.bank.service.jwt;

import com.kakao.bank.domain.repository.UserRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService{

    @Value("${jwt.auth.access}")
    String accessKey;

    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * 토큰 생성
     * @return token
     */
    @Override
    public String createToken(String id) {
        String secretKey = accessKey;

        byte[] byteKey = DatatypeConverter.parseBase64Binary(secretKey);
        SecretKeySpec signInKey = new SecretKeySpec(byteKey, signatureAlgorithm.getJcaName());

        Map<String, Object> headerMap = new HashMap<>();

        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> map = new HashMap<>();

        map.put("id", id);

        return Jwts.builder()
                .setHeader(headerMap)
                .setClaims(map)
                .signWith(signInKey)
                .compact();

    }
}
