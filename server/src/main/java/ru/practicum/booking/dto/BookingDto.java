package ru.practicum.booking.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDateTime start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDateTime end;
    @JsonProperty(access = WRITE_ONLY)
    Long itemId;
    UserDto booker;
    ItemDto item;
    Status status;


}
