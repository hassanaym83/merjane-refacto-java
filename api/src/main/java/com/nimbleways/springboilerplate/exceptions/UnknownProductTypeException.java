package com.nimbleways.springboilerplate.exceptions;

public class UnknownProductTypeException extends RuntimeException {
    public UnknownProductTypeException() {
        super("Unknown product type");
    }
}
