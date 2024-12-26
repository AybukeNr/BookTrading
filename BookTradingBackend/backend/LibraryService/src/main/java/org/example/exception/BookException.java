package org.example.exception;


import lombok.Getter;

@Getter
public class BookException extends RuntimeException {
    private final ErrorType errorType;
    public BookException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
    public BookException(ErrorType errorType,String message) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

}
