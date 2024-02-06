package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import java.util.List;

public interface ItemServiceInterface {
    ItemDto addItem(ItemDto itemDto, int sharerUserId);

    ItemDto updateItem(ItemDto itemDto, int sharerUserId, int itemId);

    ItemDtoResponse getItemById(Integer itemId, Integer userId);

    List<ItemDtoResponse> getAllItemsByOwnerId(int id);

    List<ItemDto> searchItem(String keyWord);

    ItemDtoResponse getItem(Integer itemId, Integer id);

    List<ItemDtoResponse> getItemUser(Integer userId);

    CommentDto addComment(Integer idUser, Integer itemId, CommentDto commentDto);
}


