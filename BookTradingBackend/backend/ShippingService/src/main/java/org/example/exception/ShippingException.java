package org.example.exception;

import lombok.Getter;

@Getter
public class ShippingException extends RuntimeException {
    private final ErrorType errorType;
    public ShippingException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public ShippingException(ErrorType errorType, String message){
        super(message);
        this.errorType = errorType;
    }

}