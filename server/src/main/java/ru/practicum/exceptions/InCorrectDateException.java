package ru.practicum.exceptions;

public class InCorrectDateException extends RuntimeException {
    public InCorrectDateException(String message) {
        super(message);
    }
}
