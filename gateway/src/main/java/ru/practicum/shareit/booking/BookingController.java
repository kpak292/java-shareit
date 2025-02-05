package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    @Autowired
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(
            @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
            @RequestParam(defaultValue = "ALL", required = false) BookingState state) {
        log.info("GET/bookings from User {} with state {}", userId, state);

        return bookingClient.getBookings(userId, state);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
                                           @PathVariable long bookingId) {
        log.info("GET/bookings/{} from User {}", bookingId, userId);

        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllByOwner(
            @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
            @RequestParam(defaultValue = "ALL", required = false) BookingState state) {
        log.info("GET/bookings/owner from User {} with state {}", userId, state);

        return bookingClient.getBookingByOwner(userId, state);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
                                         @Valid @RequestBody BookingRequestDto bookingDto) {

        log.info("POST/bookings from User {}", userId);

        return bookingClient.bookItem(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
                                          @RequestParam(required = true) boolean approved,
                                          @PathVariable long bookingId) {
        log.info("PATCH/bookings/{} from User {} with approval {}", bookingId, userId, approved);

        return bookingClient.approveBooking(userId, bookingId, approved);
    }
}
