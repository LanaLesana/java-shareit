package ru.practicum.exceptions;

public class InCorrectBookingException extends RuntimeException {
    public InCorrectBookingException(String message) {
        super(message);
    }
}
