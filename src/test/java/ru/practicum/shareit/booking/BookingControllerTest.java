package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingServiceInterface bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addItem_ShouldCreateBooking() throws Exception {
        User user1 = User.builder()
                .id(1)
                .email("user1@gmail.com")
                .name("User1")
                .build();

        BookingDto bookingDto1 = BookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.now().plusMonths(2))
                .build();

        Item item1 = Item.builder()
                .id(1)
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(user1)
                .build();

        Booking booking1 = Booking.builder()
                .item(item1)
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.now().plusMonths(2))
                .build();

        int bookerId = 1;

        given(bookingService.addBooking(any(BookingDto.class), eq(bookerId))).willReturn(booking1);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto1))
                        .header("X-Sharer-User-Id", bookerId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(booking1)));

        verify(bookingService).addBooking(any(BookingDto.class), eq(bookerId));
    }

    @Test
    void updateBooking_ShouldUpdateBookingStatus() throws Exception {
        Integer id = 1;
        Integer bookingId = 1;
        Boolean approved = true;

        User user1 = User.builder()
                .id(1)
                .email("user1@gmail.com")
                .name("User1")
                .build();

        Item item1 = Item.builder()
                .id(1)
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(user1)
                .build();

        Booking booking1 = Booking.builder()
                .item(item1)
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.now().plusMonths(2))
                .build();

        given(bookingService.updateBooking(bookingId, id, approved)).willReturn(booking1);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", approved.toString())
                        .header("X-Sharer-User-Id", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(booking1)));

        verify(bookingService).updateBooking(bookingId, id, approved);
    }

    @Test
    void getBooking_ShouldReturnBooking() throws Exception {
        User user1 = User.builder()
                .id(1)
                .email("user1@gmail.com")
                .name("User1")
                .build();

        Item item1 = Item.builder()
                .id(1)
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(user1)
                .build();

        Booking booking1 = Booking.builder()
                .item(item1)
                .start(LocalDateTime.of(2024, Month.FEBRUARY, 15, 10, 0))
                .end(LocalDateTime.of(2024, Month.MARCH, 15, 10, 0))
                .build();
        booking1.setId(1);
        given(bookingService.getBooking(booking1.getId(), user1.getId())).willReturn(booking1);
        mockMvc.perform(get("/bookings/{bookingId}", booking1.getId())
                        .header("X-Sharer-User-Id", user1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(booking1)));

        verify(bookingService).getBooking(booking1.getId(), user1.getId());

    }

    @Test
    void getAllBookingUser_WithUnsupportedStatus_ShouldThrowException() throws Exception {
        Integer id = 1;
        String state = "UNSUPPORTED_STATUS";

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", id)
                        .param("state", state))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnsupportedStatusException));

        verify(bookingService, never()).getAllBookingUsers(anyInt(), anyInt(), anyInt());
        verify(bookingService, never()).getBookingByState(anyString(), anyInt());
    }
}