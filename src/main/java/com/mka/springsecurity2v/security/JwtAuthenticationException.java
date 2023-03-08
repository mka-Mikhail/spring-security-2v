package com.mka.springsecurity2v.security;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

//класс для отлова иксключений при работе с токеном
@Getter
public class JwtAuthenticationException extends AuthenticationException {
    private HttpStatus httpStatus;

    public JwtAuthenticationException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
