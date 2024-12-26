package org.example.exception;


import lombok.Getter;

@Getter
public class ListException extends RuntimeException {
    private final ErrorType errorType;
    public ListException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
    public ListException(ErrorType errorType,String message) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

}
