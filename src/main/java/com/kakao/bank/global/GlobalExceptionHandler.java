package com.kakao.bank.global;

import com.kakao.bank.domain.response.Response;
import com.kakao.bank.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Response> httpClientErrorExceptionHandler(HttpClientErrorException e) {
        log.warn("httpClientErrorExceptionHandler()");
        log.warn(e.getStatusCode().toString() + " " + e.getMessage());
        Response response = new Response(e.getStatusCode().value(), e.getMessage());
        return new ResponseEntity<>(response, e.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> exceptionHandler(Exception e) {
        log.warn("exceptionHandler()");
        e.printStackTrace();
        Response response = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Response> bindExceptionHandler(BindException e) {
        log.warn(e.getMessage());

        log.warn(e.getBindingResult().toString());
        Response response = new Response(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Response> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.warn("httpRequestMethodNotSupportedExceptionHandler()");
        log.warn(e.getMessage());

        Response response = new Response(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response> customExceptionHandler(CustomException e) {
        log.warn("customExceptionHandler()");
        log.warn(e.getMessage());

        Response response = new Response(e.getStatus().value(), e.getMessage());
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<Response> parseExceptionHandler(ParseException e) {
        log.warn("parseExceptionHandler()");
        log.warn(e.getMessage());

        Response response = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> validationExceptionHandler(MethodArgumentNotValidException e) {
        log.warn("validationExceptionHandler()");
        log.warn(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        Response response = new Response(HttpStatus.BAD_REQUEST.value(),
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        log.warn("httpMessageNotReadableExceptionHandler()");
        log.warn(e.getLocalizedMessage());

        Response response = new Response(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
        log.warn("dataIntegrityViolationExceptionHandler()");

        Response response = new Response(HttpStatus.BAD_REQUEST.value(), "중복된 값");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
