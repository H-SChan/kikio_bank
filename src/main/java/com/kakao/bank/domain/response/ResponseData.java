package com.kakao.bank.domain.response;

import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
public class ResponseData<T> extends Response {
    private T data;

    public ResponseData(HttpStatus status, String massage, T data) {
        super(status, massage);
        this.data = data;
    }
}
