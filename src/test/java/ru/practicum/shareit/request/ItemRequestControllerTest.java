package ru.practicum.shareit.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Integer userId = 1;
    private RequestDto requestDto;
    private RequestDtoWithRequest requestDtoWithRequest;

    @BeforeEach
    void setUp() {
        UserDto requester = new UserDto(1, "New User", "newuser@example.com");
        List<ItemDtoRequest> items = Arrays.asList(
                new ItemDtoRequest(1, "Name", "Description", 5, 1, true),
                new ItemDtoRequest(2, "Name2", "Description2", 5, 1, true)
        );
        this.requestDto = RequestDto.builder()
                .id(1)
                .items(items)
                .created(LocalDateTime.now())
                .description("Test description RequestDto")
                .requester(requester)
                .build();
        this.requestDtoWithRequest = RequestDtoWithRequest.builder()
                .id(1)
                .items(items)
                .requester(requester)
                .description("Description")
                .created(LocalDateTime.now())
                .build();
    }


    @Test
    void addItemRequestTest() throws Exception {
        Mockito.when(itemRequestService.addItemRequest(any(RequestDto.class), eq(userId))).thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
    }

    @Test
    void getRequestsTest() throws Exception {
        List<RequestDtoWithRequest> requests = Arrays.asList(requestDtoWithRequest);
        Mockito.when(itemRequestService.getItemRequest(userId)).thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requests)));
    }

    @Test
    void getAllRequestsTest() throws Exception {
        List<RequestDtoWithRequest> requests = Arrays.asList(requestDtoWithRequest);
        Mockito.when(itemRequestService.getAllItemRequest(userId, 0, 20)).thenReturn(requests);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requests)));
    }

    @Test
    void getRequestByIdTest() throws Exception {
        Mockito.when(itemRequestService.getRequestById(userId, requestDtoWithRequest.getId())).thenReturn(requestDtoWithRequest);

        mockMvc.perform(get("/requests/{requestId}", requestDtoWithRequest.getId())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requestDtoWithRequest)));
    }
}