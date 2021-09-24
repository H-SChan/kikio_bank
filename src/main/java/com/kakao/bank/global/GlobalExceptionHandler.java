package com.kakao.bank.global;

import com.kakao.bank.domain.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Response> httpClientErrorExceptionHandler(HttpClientErrorException e) {
        log.warn("httpClientErrorExceptionHandler()");
        log.warn(e.getStatusCode().toString() + " " + e.getMessage());
        Response response = new Response(e.getStatusCode(), e.getMessage());
        return new ResponseEntity<>(response, e.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> exceptionHandler(Exception e) {
        log.warn("exceptionHandler()");
        e.printStackTrace();
        Response response = new Response(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
