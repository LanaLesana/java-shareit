package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.db.JpaUserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private JpaUserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private User user;
    private Request request;
    private RequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Test User");

        request = new Request();
        request.setId(1);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());

        requestDto = new RequestDto();
    }

    @Test
    void addItemRequest_WithValidUser_ShouldCreateRequest() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any(Request.class))).thenReturn(request);

        RequestDto result = itemRequestService.addItemRequest(requestDto, user.getId());

        assertNotNull(result);

        verify(userRepository, times(1)).findById(user.getId());
        verify(itemRequestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void addItemRequest_WithInvalidUser_ShouldThrowException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> itemRequestService.addItemRequest(requestDto, 2));

        verify(userRepository, times(1)).findById(2);
        verify(itemRequestRepository, never()).save(any(Request.class));
    }

    @Test
    void getItemRequest_WithValidUser_ShouldReturnRequests() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequesterId(anyInt())).thenReturn(List.of(request));

        List<RequestDtoWithRequest> result = itemRequestService.getItemRequest(user.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());

        verify(userRepository, times(1)).findById(user.getId());
        verify(itemRequestRepository, times(1)).findAllByRequesterId(user.getId());
    }

    @Test
    void getItemRequest_WithInvalidUser_ShouldThrowException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> itemRequestService.getItemRequest(2));

        verify(userRepository, times(1)).findById(2);
        verify(itemRequestRepository, never()).findAllByRequesterId(anyInt());
    }
}