package org.example.exception;

import lombok.Getter;

@Getter
public class OfferException extends RuntimeException {
    private final ErrorType errorType;
    public OfferException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
    public OfferException(ErrorType errorType,String message) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

}
