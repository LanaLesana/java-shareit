package ru.practicum.shareit.exception;


public class UnsupportedStatusException extends RuntimeException {
    public UnsupportedStatusException(String status) {
        super("Unknown state: " + status);
    }
}
