package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingServiceInterface {
    Booking addBooking(BookingDto bookingDto, Integer bookerId);

    Booking updateBooking(Integer bookingId, Integer userId, Boolean approved);

    Booking getBooking(Integer bookingId, Integer id);

    List<Booking> getAllBookingUsers(Integer userId);

    List<Booking> getBookingByState(String state, Integer id);

    List<Booking> getBookingByOwner(String state, Integer ownerId);


}
