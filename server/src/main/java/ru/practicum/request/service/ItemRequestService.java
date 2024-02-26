package ru.practicum.request.service;

import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestDtoWithRequest;

import java.util.List;

public interface ItemRequestService {
    RequestDto addItemRequest(RequestDto requestDto, Long userId);

    List<RequestDtoWithRequest> getItemRequest(Long userId);

    List<RequestDtoWithRequest> getAllItemRequest(Long userId, int from, int size);

    RequestDtoWithRequest getRequestById(Long userId, Long requestId);
}