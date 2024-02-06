package ru.practicum.shareit.item.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Component
public class ItemValidation {

    public void checkUserId(User user) {
        boolean checkUser = false;

        if (user != null) {
            checkUser = true;

        }
        if (!checkUser) {
            throw new NotFoundException("Пользователь с id не найден");
        }
    }

    public void checkItemId(Item item) {
        boolean checkUser = false;

        if (item != null) {
            checkUser = true;

        }
        if (!checkUser) {
            throw new NotFoundException("Пользователь с id не найден");
        }
    }


    public void checkingDataNull(ItemDtoResponse item) {

        if (item == null) {
            throw new NotFoundException("Вещь не найдена ");
        }
    }

    public void checkItem(ItemDto itemDto) {
        if (itemDto.getAvailable() == null) {
            throw new NotFoundException("Получены не все данные ");
        }

        if (itemDto.getName().length() == 0) {
            throw new NotFoundException("Имя не может быть пустым");
        }

        if (itemDto.getDescription() == null) {
            throw new NotFoundException("Нет описание вещи ");
        }
    }

    public void checkItemUpdate(Integer idUser, Item item) {

        if (item.getOwner().getId() != idUser) {
            throw new BadRequestException("Невозможно обновить данные");
        }
    }

    public void checkComment(String text, LocalDateTime start) {
        if (text.isEmpty()) {
            throw new BadRequestException("Нет комментария ");
        }
        LocalDateTime localDateTime = LocalDateTime.now();

        if (start.isBefore(localDateTime)) {
            throw new BadRequestException("Неверный статус ");
        }
    }

    public void checkCommentBooking(Integer userId, List<Booking> bookings) {

        if (bookings.size() == 0) {
            throw new BadRequestException("Пользователь" + userId + " не брал вишь в аренду ");
        }
        for (Booking booking : bookings) {
            if (booking.getStatus() == Status.REJECTED) {
                throw new BadRequestException("Бронирование отклонено ");
            }
        }


    }
}