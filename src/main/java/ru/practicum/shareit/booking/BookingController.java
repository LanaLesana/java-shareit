package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping
@AllArgsConstructor
@Slf4j
public class BookingController {
    @Autowired
    private final BookingServiceInterface bookingService;

    @PostMapping("/bookings")
    public Booking addItem(@RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") int bookerId) {
        log.info("Дата начала {} и дата конца {}", bookingDto.getStart(), bookingDto.getEnd());
        return bookingService.addBooking(bookingDto, bookerId);
    }

    @PatchMapping("/bookings/{bookingId}")
    public Booking updateBooking(@RequestHeader("X-Sharer-User-Id") Integer id,
                                 @PathVariable Integer bookingId,
                                 @RequestParam(name = "approved", required = false) Boolean approved) {
        log.info("Заголовок {}, id бронирования {}, значение {}", id, bookingId, approved);
        return bookingService.updateBooking(bookingId, id, approved);
    }

    @GetMapping("/bookings/{bookingId}")
    public Booking getBooking(@RequestHeader("X-Sharer-User-Id") Integer id,
                              @PathVariable Integer bookingId) {
        log.info("Вызов метода получения информации по бронированию. Заголовок {}, вещь {}", id, bookingId);
        return bookingService.getBooking(bookingId, id);
    }

    @GetMapping("/bookings")
    public List<Booking> getAllBookingUser(@RequestHeader("X-Sharer-User-Id") Integer id,
                                           @RequestParam(name = "state", required = false) String state,
                                           @RequestParam(required = false, defaultValue = "0") @Min(0) int from,
                                           @RequestParam(required = false, defaultValue = "10") @Min(0) int size) {
        log.info("Вызов метода получения информации. Заголовок {}, Статус {}", id, state);
        if (state == null) {
            return bookingService.getAllBookingUsers(id, from, size);
        } else if (state.equals("UNSUPPORTED_STATUS")) {
            throw new UnsupportedStatusException("UNSUPPORTED_STATUS");
        } else {
            return bookingService.getBookingByState(state, id);
        }

    }

    @GetMapping("/bookings/owner")
    public List<Booking> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                           @RequestParam(name = "state", required = false) String state,
                                           @RequestParam(required = false, defaultValue = "0") @Min(0) int from,
                                           @RequestParam(required = false, defaultValue = "10") @Min(0) int size) {
        log.info("Вызов метода получения информации об Owner. Заголовок {}, Статус {}", ownerId, state);

        return bookingService.getBookingByOwner(state, ownerId, from, size);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(ConflictException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @RestControllerAdvice
        public class ErrorHandler {
            @ExceptionHandler(UnsupportedStatusException.class)
            @ResponseStatus(HttpStatus.BAD_REQUEST)
            public Map<String, String> validationException(final Exception e) {
                return Map.of("error", e.getMessage());
            }
        }
    }
}
