package ru.practicum.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.booking.dto.BookingDtoShort;

import java.util.Comparator;
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
public class ItemDto implements Comparator<ItemDto> {


    Long id;
    @NotBlank(message = "Name не должен быть пустым")
    String name;
    @NotBlank(message = "description не должен быть пустым")
    String description;
    @NotNull(message = "available не должен отсутствовать")
    Boolean available;
    List<CommentDto> comments;
    BookingDtoShort lastBooking;
    BookingDtoShort nextBooking;
    Long requestId;

    @Override
    public int compare(ItemDto item1, ItemDto item2) {

        return Long.compare(item1.getId(), item2.getId());
    }

}