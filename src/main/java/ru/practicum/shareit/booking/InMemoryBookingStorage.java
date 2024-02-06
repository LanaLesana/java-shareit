package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.HashMap;

public class InMemoryBookingStorage {
    private HashMap<Integer, Booking> bookingHashMap = new HashMap<>();
    private Integer generatedBookingId;
}
