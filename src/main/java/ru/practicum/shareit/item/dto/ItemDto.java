package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.comment.CommentDto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    Integer id;
    @NotBlank(message = "Name не должен быть пустым")
    String name;
    @NotBlank(message = "description не должен быть пустым")
    String description;
    Integer ownerId;
    @NotNull(message = "available не должен отсутствовать")
    Boolean available;
    List<CommentDto> comments;
    BookingDtoShort lastBooking;
    BookingDtoShort nextBooking;
    Integer requestId;

}
