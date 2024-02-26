package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.item.dto.ItemDtoReq;
import ru.practicum.user.dto.UserDto;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {
    Long id;
    String description;
    UserDto requestor;
    @JsonProperty("created")
    LocalDateTime created;
    List<ItemDtoReq> items;
}