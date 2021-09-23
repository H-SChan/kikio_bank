package com.kakao.bank.domain.response;

import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
public class Response {
    private HttpStatus status;
    private String massage;

    public Response(HttpStatus status, String massage) {
        this.status = status;
        this.massage = massage;
    }
}
