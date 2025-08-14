package com.smooth.usage_example.common_example.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    Integer getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
