package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;

import java.util.List;

public interface ItemRequestService {
    RequestDto addItemRequest(RequestDto requestDto, Integer userId);

    List<RequestDtoWithRequest> getItemRequest(Integer userId);

    List<RequestDtoWithRequest> getAllItemRequest(Integer userId, int from, int size);

    RequestDtoWithRequest getRequestById(Integer userId, Integer requestId);
}
