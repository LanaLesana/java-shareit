package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestDtoWithRequest;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

@UtilityClass
public class RequestMapper {
    public static Request toItemRequest(User user, RequestDto requestDto) {
        return Request.builder()
                .description(requestDto.getDescription())
                .requestor(user)
                .build();
    }

    public static RequestDto toItemRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }

    public static RequestDtoWithRequest toRequestDtoWithRequest(Request request) {
        return RequestDtoWithRequest.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(ItemMapper.toItemDtoList(request.getItems()))
                .build();
    }


}
