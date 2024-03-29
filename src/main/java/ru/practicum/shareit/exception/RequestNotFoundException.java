package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RequestNotFoundException extends ResponseStatusException {
    public RequestNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}