package org.example.exception;

import lombok.Getter;

@Getter
public class TransactionException extends RuntimeException {
    private final ErrorType errorType;
    public TransactionException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
    public TransactionException(ErrorType errorType,String message) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

}
