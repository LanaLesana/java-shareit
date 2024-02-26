package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.dto.ItemDtoReq;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDtoWithRequest {
    Long id;
    String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDateTime created;
    List<ItemDtoReq> items;
}
