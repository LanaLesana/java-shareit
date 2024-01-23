package ru.practicum.shareit.booking.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingNoTimeStamp;
import ru.practicum.shareit.booking.status.Status;

import java.util.List;


@Component
@Slf4j
@AllArgsConstructor
public class BookingStorage {
    private final JpaBookingRepository jpaBooking;

    public Booking addBooking(Booking booking) {
        jpaBooking.save(booking);
        return jpaBooking.findAllBookingsWithItemAndUserById(booking.getId());
    }

    public BookingNoTimeStamp updateBooking(Booking booking) {
        jpaBooking.save(booking);
        BookingNoTimeStamp bookingNoTimestamp = BookingNoTimeStamp.builder()
                .id(booking.getId())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .status(booking.getStatus())
                .build();
        bookingNoTimestamp.setStart("undefined");
        bookingNoTimestamp.setEnd("undefined");
        return bookingNoTimestamp;
    }

    //    public Booking getBookingById(Integer id) {
//        return jpaBooking.getBookingByIdOrderByStart(id);
//    }
    public Booking getBookingById(Integer id) {
        return jpaBooking.findBookingById(id);
    }

    public List<Booking> getAllBookingUsers(Integer id) {
        return jpaBooking.findAllByBooker_IdOrderByStartDesc(id);
    }


    public List<Booking> getBookingByOwner(Integer ownerId) {

        return jpaBooking.findBookingByItem_OwnerIdOrderByStartDesc(ownerId);
    }

    public List<Booking> findBookingsWithFutureStartTime(Integer id) {
        log.info("Возвращаю все бронирования из будущего.");
        return jpaBooking.findBookingsWithFutureStartTime(id);
    }

    public List<Booking> findAllByStatus(Status status) {
        return jpaBooking.findAllByStatus(status);
    }

    public List<Booking> findAllByItem_OwnerOrStatus(Integer id, Status status) {
        return jpaBooking.findAllByBooker_IdAndStatus(id, status);
    }

    public List<Booking> findAllByItem_OwnerOrStatusWaiting(Integer id, Status status) {
        return jpaBooking.findAllBookingsByItem_OwnerIdAndStatus(id, status);
    }


}
