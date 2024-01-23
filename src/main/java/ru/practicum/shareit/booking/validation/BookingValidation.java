package ru.practicum.shareit.booking.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class BookingValidation {

    public void checkItemAvailable(BookingDto bookingDto, Item item) {
        LocalDateTime localDate = LocalDateTime.now();
        if (item == null) {
            throw new NotFoundException("Вещь не найдена");
        }
        if (bookingDto.getEnd() == null || bookingDto.getStart() == null) {
            throw new BadRequestException("Не заполнено время");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BadRequestException("Время начала бронирования позже конца");
        }
        if (bookingDto.getEnd().isEqual(bookingDto.getStart()) && bookingDto.getStart().isAfter(localDate)) {
            throw new BadRequestException("Неправильная дата");
        }
        if (bookingDto.getStart().isBefore(localDate)) {
            throw new BadRequestException("Неправильная дата");
        }
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь недоступна для бронирования");
        }
    }

    public void bookerValidation(User user) {
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public void checkBookerOrOwner(List<Booking> bookingUser, List<Booking> booking) {
        if (booking.isEmpty() && bookingUser.isEmpty()) {
            throw new BadRequestException("Нет разрешения для просмотра");
        }
    }

    public void checkBooking(Booking booking) {
        if (booking == null) {
            throw new NotFoundException("Вещь не найдена");
        }
    }

    public void checkBookerOrOwnerUser(User user) {
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public void checkUpdateBooking(Integer idUser, Boolean approved, Booking booking) {
        if (approved == null) {
            throw new BadRequestException("Нет данных для изменения");
        }
        if (booking.getItem().getOwner().getId() != idUser) {
            throw new NotFoundException("Нет разрешения на изменение");
        }
    }

    public void checkIdBookerUpdate(Boolean approved, Booking booking) {
        log.info("Получен статус" + booking.getStatus());
        if (approved && booking.getStatus() == Status.APPROVED) {
            throw new BadRequestException("Данные уже обновлены ");
        }
    }

    public void checkOwner(Integer ownerId, Item itemCheckOwner) {
        if (itemCheckOwner.getOwner().getId() == ownerId) {
            throw new NotFoundException("Ошибка бронирования ");
        }
    }
    public void checkOwnerAndBookerMatch(Integer ownerId, Booking booking) {
        if (booking.getBooker().getId() != ownerId && booking.getItem().getOwner().getId() != ownerId) {
            throw new NotFoundException("Ошибка бронирования ");
        }
    }

}