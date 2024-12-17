package org.example.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private final ErrorType errorType;
    public AuthException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public AuthException(ErrorType errorType, String message){
        super(message);
        this.errorType = errorType;
    }

}