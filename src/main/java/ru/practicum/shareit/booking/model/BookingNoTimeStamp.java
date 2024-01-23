package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class BookingNoTimeStamp {
    private Integer id;
    private Status status;
    private User booker;
    private Item item;
    private String start;
    private String end;


}
