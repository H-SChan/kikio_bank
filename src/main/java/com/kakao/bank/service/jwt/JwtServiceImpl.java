package com.kakao.bank.service.jwt;

import com.kakao.bank.domain.entity.User;
import com.kakao.bank.domain.repository.UserRepo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService{

    @Value("${jwt.auth.access}")
    String accessKey;

    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final UserRepo userRepo;

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

    /**
     * 토큰 유효 검증
     * @return userId
     */
    @Override
    @Transactional(readOnly = true)
    public String validToken(String token) {
        try {
            byte[] byteKey = DatatypeConverter.parseBase64Binary(accessKey);
            Key signInKey = new SecretKeySpec(byteKey, signatureAlgorithm.getJcaName());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signInKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return userRepo.findById(claims.get("id").toString()).orElseThrow(
                    () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "유저 없음")
            ).getId();
        } catch (ExpiredJwtException e) {
            throw new HttpClientErrorException(HttpStatus.GONE, "토큰 만료");
        } catch (SignatureException | MalformedJwtException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰 위조");
        } catch (IllegalArgumentException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "토큰 없음");
        } catch (HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
        }
    }
}
