package ru.practicum.shareit.booking.dto.exceptions;

public class NotAvailableException extends RuntimeException {
    public NotAvailableException(String message) {
        super(message);
    }
}