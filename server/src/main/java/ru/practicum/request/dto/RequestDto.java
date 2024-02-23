package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.item.dto.ItemDtoReq;
import ru.practicum.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "не должно быть пустым")
    String description;
    UserDto requestor;
    @JsonProperty("created")
    LocalDateTime created;
    List<ItemDtoReq> items;
}