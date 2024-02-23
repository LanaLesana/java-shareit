package ru.practicum.item.service;

import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Item saveItem(ItemDto itemDto, Long userId);

    Item updateItem(ItemDto itemDto, Long userId);

    ItemDto getItemById(Long userId, Long itemId);

    List<ItemDto> getItemsByUser(Long userId, int from, int size);

    Collection<ItemDto> searchItem(String text, int from, int size);

    CommentDto postComment(Long userId, Long itemId, CommentDto commentDto);

    void deleteItemById(Long userId, Long itemId);
}