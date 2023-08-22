package com.zs.oauth2.handler;

import com.zs.ApiRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 35536
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ApiRestResponse<String> handleInvalidTokenException(InvalidTokenException e) {
        return ApiRestResponse.error(e.getMessage());
    }

    @ExceptionHandler(ClientAuthenticationException.class)
    public ApiRestResponse<String> handlerClientAuthenticationException(RuntimeException e) {
        return ApiRestResponse.error(e.getMessage());

    }
}
