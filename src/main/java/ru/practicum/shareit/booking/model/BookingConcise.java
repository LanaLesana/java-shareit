package ru.practicum.shareit.booking.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class BookingConcise {
    Integer id;
    Integer bookerId;
}