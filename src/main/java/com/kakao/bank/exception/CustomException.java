package com.kakao.bank.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
}
