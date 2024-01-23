package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemStorageInterface {
    ItemDto addItem(ItemDto item);

    ItemDto updateItem(ItemDto item);

    ItemDto getItemById(int id);

    List<ItemDto> getAllItemsByOwnerId(int id);

    List<ItemDto> searchItem(String keyWord);
}
