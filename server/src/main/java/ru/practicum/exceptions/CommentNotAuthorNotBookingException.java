package ru.practicum.exceptions;

public class CommentNotAuthorNotBookingException extends RuntimeException {
    public CommentNotAuthorNotBookingException(String message) {
        super(message);
    }
}