package com.nimbleways.springboilerplate.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(final String resourceName) {
        super(resourceName + " not found");
    }
}
