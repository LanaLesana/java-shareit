package ru.practicum.shareit.booking.db;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaBookingRepository extends PagingAndSortingRepository<Booking, Integer> {
    Booking getBookingByIdOrderByStart(Integer id);

    @Query(value = "SELECT NEW Booking ( b.id, b.start , b.end, i," +
            "    u, " +
            "    b.status " +
            ") " +
            "FROM Booking b " +
            "JOIN b.item i " +
            "JOIN b.booker u " +
            "WHERE b.id = :bookingId")
    Booking findAllBookingsWithItemAndUserById(@Param("bookingId") Integer bookingId);


    List<Booking> findAllByBooker_IdOrderByStartDesc(Integer id, Pageable size);
    List<Booking> findAllByBooker_IdOrderByStartDesc(Integer id);

    @Query(value = "SELECT b FROM Booking b WHERE b.start > CURRENT_TIMESTAMP ORDER BY b.start desc ")
    List<Booking> findBookingsWithFutureStartTime(Integer id);


    List<Booking> findAllByStatus(Status status);

    List<Booking> findAllBookingsByItem_OwnerIdAndStatus(Integer ownerId, Status status);

    List<Booking> findAllByBooker_IdAndStatus(Integer id, Status status);

    List<Booking> findBookingByItem_OwnerIdOrderByStartDesc(Integer ownerId, Pageable pageable);
    List<Booking> findBookingByItem_OwnerIdOrderByStartDesc(Integer ownerId);

    Booking findBookingById(Integer id);

    Booking findFirstByItemIdAndStatusAndStartIsAfterOrStartEqualsOrderByStart(Integer id, Status status, LocalDateTime localDateTime,
                                                                               LocalDateTime localDateTime1);

    Booking findFirstByItemIdAndStatusAndStartIsBeforeOrStartEqualsOrderByEndDesc(Integer id, Status status, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    List<Booking> findAllByBooker_IdAndItem_Id(Integer userId, Integer itemId);
}
