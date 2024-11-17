package ru.practicum.workshop.userservice.exception;

public class EntityValidationException extends RuntimeException {

    public EntityValidationException(String message) {
        super(message);
    }
}
