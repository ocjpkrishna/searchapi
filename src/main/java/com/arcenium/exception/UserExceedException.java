package com.arcenium.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class UserExceedException extends RuntimeException {
    public UserExceedException(String message) {
        super(message);
    }
}
