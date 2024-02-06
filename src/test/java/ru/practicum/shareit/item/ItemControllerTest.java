package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemServiceInterface itemService;

    @Test
    void addItem_ShouldAddItem() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("testName1")
                .available(true)
                .description("testDescription1")
                .ownerId(1)
                .build();
        ItemDto returnedDto = ItemDto.builder()
                .id(1)
                .name("testName1")
                .available(true)
                .description("testDescription1")
                .ownerId(1)
                .build();

        Mockito.when(itemService.addItem(Mockito.any(ItemDto.class), Mockito.anyInt())).thenReturn(returnedDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .content(new ObjectMapper().writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(returnedDto)));

        Mockito.verify(itemService, Mockito.times(1)).addItem(Mockito.any(ItemDto.class), Mockito.anyInt());
    }

    @Test
    public void updateItem_ShouldUpdateItem() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("testName1")
                .available(true)
                .description("testDescription1")
                .ownerId(1)
                .build();
        ItemDto updatedDto = ItemDto.builder()
                .id(1)
                .name("testName1Updated")
                .available(true)
                .description("testDescription1")
                .ownerId(1)
                .build();

        Mockito.when(itemService.updateItem(Mockito.any(ItemDto.class), Mockito.anyInt(), Mockito.anyInt())).thenReturn(updatedDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", 1)
                        .content(new ObjectMapper().writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(updatedDto)));

        Mockito.verify(itemService, Mockito.times(1)).updateItem(Mockito.any(ItemDto.class), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void getItemById_ShouldReturnItem() throws Exception {
        ItemDtoResponse itemDtoResponse = new ItemDtoResponse();

        Mockito.when(itemService.getItemById(Mockito.anyInt(), Mockito.anyInt())).thenReturn(itemDtoResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(itemDtoResponse)));

        Mockito.verify(itemService, Mockito.times(1)).getItemById(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void addComment_ShouldAddCommentToItem() throws Exception {
        Integer userId = 1;
        Integer itemId = 1;
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .authorName("Author name")
                .itemId(1)
                .build();

        commentDto.setText("Great Item!");

        CommentDto returnedDto = CommentDto.builder()
                .id(1)
                .authorName("Author name")
                .itemId(1)
                .build();
        returnedDto.setText("Great Item!");

        Mockito.when(itemService.addComment(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(CommentDto.class)))
                .thenReturn(returnedDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/items/{itemId}/comment", itemId)
                        .content(new ObjectMapper().writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(returnedDto)));

        Mockito.verify(itemService).addComment(userId, itemId, commentDto);
    }
}