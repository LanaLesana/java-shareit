package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.db.JpaUserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.request.RequestMapper.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final JpaUserRepository userRepository;

    @Override
    public RequestDto addItemRequest(RequestDto requestDto, Integer userId) {
        Optional<User> newUser = userRepository.findById(userId);
        if (newUser.isPresent()) {
            User user = newUser.get();

            Request request = toItemRequest(user, requestDto);
            request.setRequester(user);
            request.setCreated(LocalDateTime.now());

            return toItemRequestDto(itemRequestRepository.save(request));
        } else {
            throw new UserNotFoundException("Пользователь с id " + "userId" + "не найден");
        }
    }


    @Override
    public List<RequestDtoWithRequest> getItemRequest(Integer userId) {
        User requester = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        List<RequestDtoWithRequest> requestDtoWithRequests =
                itemRequestRepository.findAllByRequesterId(userId).stream()
                        .map(request -> toRequestDtoWithRequest(request))
                        .collect(Collectors.toList());
        for (RequestDtoWithRequest withRequest : requestDtoWithRequests) {
            for (ItemDtoRequest item : withRequest.getItems()) {
                item.setRequestId(withRequest.getId());
            }
        }
        return requestDtoWithRequests;
    }

    @Override
    public List<RequestDtoWithRequest> getAllItemRequest(Integer userId, int from, int size) {
        if ((from < 0 || size < 0 || (from == 0 && size == 0))) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "неверный параметр пагинации");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        User requester = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        List<Request> byOwnerId = itemRequestRepository.findByOwnerId(userId, pageable);
        List<RequestDtoWithRequest> requestDtoWithRequests =
                byOwnerId.stream()
                        .map(request -> {
                            return toRequestDtoWithRequest(request);
                        })
                        .collect(Collectors.toList());
        for (RequestDtoWithRequest withRequest : requestDtoWithRequests) {
            for (ItemDtoRequest item : withRequest.getItems()) {
                item.setRequestId(withRequest.getId());
            }
        }
        return requestDtoWithRequests;
    }

    @Override
    public RequestDtoWithRequest getRequestById(Integer userId, Integer requestId) {
        User requester = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        Request request = itemRequestRepository.findById(requestId).orElseThrow(() ->
                new RequestNotFoundException(HttpStatus.NOT_FOUND, "Запрос предмета по id не найден"));
        RequestDtoWithRequest requestDtoWithRequest = toRequestDtoWithRequest(request);
        for (ItemDtoRequest item : requestDtoWithRequest.getItems()) {
            item.setRequestId(requestId);
        }
        return requestDtoWithRequest;
    }
}
