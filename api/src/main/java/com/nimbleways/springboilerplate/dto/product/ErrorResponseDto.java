package com.nimbleways.springboilerplate.dto.product;

public record ErrorResponseDto(int httpStatusCode,String message, String details) {
}
