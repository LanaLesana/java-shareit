package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.validation.Validation;

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

    Long id;
    @NotBlank(groups = {Validation.Post.class}, message = "Name не должен быть пустым")
    String name;
    @NotBlank(groups = {Validation.Post.class}, message = "description не должен быть пустым")
    String description;
    @NotNull(groups = {Validation.Post.class}, message = "available не должен отсутствовать")
    Boolean available;
    List<CommentDto> comments;
    BookingDtoShort lastBooking;
    BookingDtoShort nextBooking;
    Long requestId;

}
