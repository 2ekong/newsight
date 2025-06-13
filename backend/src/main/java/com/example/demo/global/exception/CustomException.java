package com.example.demo.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());  //RuntimeException의 메세지에 넣음
        this.errorCode = errorCode;
    }
}
