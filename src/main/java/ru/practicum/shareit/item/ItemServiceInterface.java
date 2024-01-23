package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemServiceInterface {
    ItemDto addItem(ItemDto itemDto, int sharerUserId);

    ItemDto updateItem(ItemDto itemDto, int sharerUserId, int itemId);

    ItemDto getItemById(int id);

    List<ItemDto> getAllItemsByOwnerId(int id);

    List<ItemDto> searchItem(String keyWord);
}
