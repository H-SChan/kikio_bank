package com.kakao.bank.domain.response;

import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
public class Response {
    private HttpStatus status;
    private String message;

    public Response(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
