package com.nimbleways.springboilerplate.exceptions;

import com.nimbleways.springboilerplate.dto.product.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponseDto handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                "The requested entity was not found in the database."
        );
    }

    @ExceptionHandler(UnknownProductTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponseDto handleUnknownProductTypeException(UnknownProductTypeException e) {
        return new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                "The product type provided is unknown or invalid."
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ErrorResponseDto handleIllegalArgumentException(IllegalArgumentException e) {
        return new ErrorResponseDto(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getMessage(),
                "The request contains invalid arguments."
        );
    }
}
