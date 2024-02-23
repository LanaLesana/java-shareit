package ru.practicum.booking.service;

import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto saveBooking(Long userId, BookingDto bookingDtoItem);

    Booking confirmOrCancelBooking(Long userId, Long bookingId, boolean approved);

    Booking getBookingForOwnerOrBooker(Long userId, Long bookingId);

    List<Booking> getAllBookingsForUser(Long userId, String state, boolean isOwner, int from, int size);

}
