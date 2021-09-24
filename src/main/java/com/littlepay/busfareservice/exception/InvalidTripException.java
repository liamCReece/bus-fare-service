package com.littlepay.busfareservice.exception;

public class InvalidTripException extends IllegalArgumentException {
    public InvalidTripException(String message) {
        super(message);
    }
}
