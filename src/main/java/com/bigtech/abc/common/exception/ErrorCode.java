package com.bigtech.abc.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    MEMBERNAME_DUPLICATED(HttpStatus.CONFLICT, ""),
    MEMBERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),

    INVALID_DATA(HttpStatus.NOT_FOUND, "");

    private HttpStatus httpStatus;
    private String Message;

}
