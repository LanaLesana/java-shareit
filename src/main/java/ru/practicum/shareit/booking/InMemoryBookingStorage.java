package ru.practicum.shareit.booking;

import java.util.HashMap;

public class InMemoryBookingStorage implements BookingStorage {
    private HashMap<Integer, Booking> bookingHashMap = new HashMap<>();
    private Integer generatedBookingId;
}
