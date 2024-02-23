package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest())
                .build();
    }

    public static Item toItem(User user, ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(user);
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static ItemDtoRequest toItemDtoReq(Item item) {
        return ItemDtoRequest.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwner().getId())
                .requestId(item.getRequest())
                .available(item.getAvailable())
                .build();
    }

    public static List<ItemDtoRequest> toItemDtoList(List<Item> items) {
        List<Item> safeItems = Optional.ofNullable(items).orElseGet(Collections::emptyList);

        return safeItems.stream()
                .map(ItemMapper::toItemDtoReq)
                .collect(Collectors.toList());
    }
}