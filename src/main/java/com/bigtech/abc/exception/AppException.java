package com.bigtech.abc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;
}
