package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.db.BookingStorage;
import ru.practicum.shareit.booking.db.JpaBookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.db.JpaItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.db.JpaUserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class BookingServiceImpl implements BookingServiceInterface {
    @Autowired
    BookingStorage bookingRepository;
    @Autowired
    JpaItemRepository itemRepository;
    @Autowired
    JpaUserRepository userRepository;
    @Autowired
    private final JpaBookingRepository jpaBooking;
    private final BookingValidation bookingValidation;

    @Override
    @Transactional
    public Booking addBooking(BookingDto bookingDto, Integer bookerId) {
        Item item = Item.builder().id(bookingDto.getItemId()).build();
        User user = User.builder().id(bookerId).build();
        User userBooker = userRepository.findUserById(bookerId);
        Item itemOwner = itemRepository.findItemById(bookingDto.getItemId());
        Item itemValid = itemRepository.findItemById(bookingDto.getItemId());
        if (userBooker == null) {
            throw new NotFoundException("Пользователь не найден ");
        }
        bookingValidation.checkItemAvailable(bookingDto, itemValid);
        bookingValidation.checkOwner(bookerId, itemOwner);
        bookingValidation.bookerValidation(user);

        Booking booking = Booking.builder()
                .item(item)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .booker(userBooker)
                .status(Status.WAITING)
                .build();

        return bookingRepository.addBooking(booking);
    }

    @Override
    @Transactional
    public Booking updateBooking(Integer bookingId, Integer userId, Boolean approved) {
        Booking booking = bookingRepository.getBookingById(bookingId);
        bookingValidation.checkUpdateBooking(userId, approved, booking);
        bookingValidation.checkIdBookerUpdate(approved, booking);
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingRepository.addBooking(booking);
    }

    @Override
    @Transactional
    public Booking getBooking(Integer bookingId, Integer userId) {
        Booking booking = bookingRepository.getBookingById(bookingId);
        bookingValidation.checkBooking(booking);
        bookingValidation.checkOwnerAndBookerMatch(userId, booking);

        bookingValidation.checkBookerOrOwner(bookingRepository.getAllBookingUsers(userId), bookingRepository.getBookingByOwner(userId));

        return bookingRepository.getBookingById(bookingId);
    }

    @Override
    public List<Booking> getAllBookingUsers(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
        bookingValidation.checkBookerOrOwnerUser(user);
        return bookingRepository.getAllBookingUsers(userId);
    }

    @Override
    public List<Booking> getBookingByState(String state, Integer id) {
        if (state.equals("ALL")) {
            return bookingRepository.getAllBookingUsers(id);
        }
        if (state.equals("FUTURE")) {
            return bookingRepository.findBookingsWithFutureStartTime(id);
        }
        if (state.equals("UNSUPPORTED_STATUS")) {
            throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (state.equals("WAITING")) {
            return bookingRepository.findAllByStatus(Status.WAITING);
        }

        if (state.equals("REJECTED")) {
            return bookingRepository.findAllByItem_OwnerOrStatus(id, Status.REJECTED);
        }

        if (state.equals("CURRENT")) {
            List<Booking> bookingList = bookingRepository.getAllBookingUsers(id);
            List<Booking> list = new ArrayList<>();
            LocalDateTime localDateTime = LocalDateTime.now();

            for (Booking booking : bookingList) {
                if (booking.getStart().isBefore(localDateTime) && booking.getEnd().isAfter(localDateTime)) {
                    list.add(booking);
                }
            }
            Collections.sort(list);
            return list;
        }

        if (state.equals("PAST")) {
            List<Booking> bookingList = bookingRepository.getAllBookingUsers(id);
            List<Booking> list = new ArrayList<>();
            LocalDateTime localDateTime = LocalDateTime.now();

            for (Booking booking : bookingList) {
                if (booking.getEnd().isBefore(localDateTime)) {
                    list.add(booking);
                }
            }
            return list;
        }
        return null;
    }

    @Override
    @Transactional
    public List<Booking> getBookingByOwner(String state, Integer ownerId) {
        bookingValidation.checkBookerOrOwnerUser(userRepository.findUserById(ownerId));
        if (state == null) {
            return bookingRepository.getBookingByOwner(ownerId);
        }
        if (state.equals("ALL")) {
            return bookingRepository.getBookingByOwner(ownerId);
        }
        if (state.equals("FUTURE")) {
            log.info("Делаю запрос в БД по статусу FUTURE");
            return bookingRepository.findBookingsWithFutureStartTime(ownerId);
        }
        if (state.equals("UNSUPPORTED_STATUS")) {
            throw new UnsupportedStatusException("UNSUPPORTED_STATUS");
        }
        if (state.equals("WAITING")) {
            return bookingRepository.findAllByStatus(Status.WAITING);
        }

        if (state.equals("REJECTED")) {
            return bookingRepository.findAllByItem_OwnerOrStatusWaiting(ownerId, Status.REJECTED);
        }

        if (state.equals("CURRENT")) {
            return bookingRepository.findAllByItem_OwnerOrStatusWaiting(ownerId, Status.REJECTED);
        }

        if (state.equals("PAST")) {
            List<Booking> bookingList = bookingRepository.getBookingByOwner(ownerId);
            List<Booking> list = new ArrayList<>();
            LocalDateTime localDateTime = LocalDateTime.now();

            for (Booking booking : bookingList) {
                if (booking.getEnd().isBefore(localDateTime)) {
                    list.add(booking);
                }
            }
            return list;
        }
        return null;
    }

}
