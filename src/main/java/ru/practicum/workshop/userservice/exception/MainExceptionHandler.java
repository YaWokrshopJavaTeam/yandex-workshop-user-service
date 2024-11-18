package ru.practicum.workshop.userservice.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class MainExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAuthenticationException(AuthenticationException exception) {
        ApiError apiError = new ApiError(exception.getClass().getSimpleName(), exception.getMessage());

        log.info("Exception handled: {}", apiError);

        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(ConstraintViolationException exception) {
        ApiError apiError = new ApiError(exception.getClass().getSimpleName(), exception.getMessage());

        log.info("Exception handled: {}", apiError);

        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEntityNotFoundException(EntityNotFoundException exception) {
        ApiError apiError = new ApiError(exception.getClass().getSimpleName(), exception.getMessage());

        log.info("Exception handled: {}", apiError);

        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEntityValidationException(EntityValidationException exception) {
        ApiError apiError = new ApiError(exception.getClass().getSimpleName(), exception.getMessage());

        log.info("Exception handled: {}", apiError);

        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ApiError apiError = new ApiError(exception.getClass().getSimpleName(),
                exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(" ")));

        log.info("Exception handled: {}", apiError);

        return apiError;
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception exception) {
        ApiError apiError = new ApiError("Default exception", exception.getMessage());

        log.info("Exception handled: {}", apiError);

        return apiError;
    }

}
