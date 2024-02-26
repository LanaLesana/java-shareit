package ru.practicum.booking.dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoForItem {
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
