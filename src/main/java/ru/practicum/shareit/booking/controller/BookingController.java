package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StateDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.config.Constants;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    @Autowired
    @Qualifier("bookingServiceV1")
    BookingService bookingService;

    @GetMapping
    public ResponseEntity<Collection<BookingDto>> findAllByUserId(
            @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
            @RequestParam(defaultValue = "ALL", required = false) StateDto state) {

        Collection<BookingDto> bookings = bookingService.findByUserId(userId, state);

        log.info("GET/bookings from User {} with state {} with response {}", userId, state, bookings);

        return new ResponseEntity<>(
                bookings,
                HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> findById(@RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
                                               @PathVariable long bookingId) {

        BookingDto booking = bookingService.findById(userId, bookingId);

        log.info("GET/bookings/{} from User {} with response {}", bookingId, userId, booking);

        return new ResponseEntity<>(
                booking,
                HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<Collection<BookingDto>> findAllByOwner(
            @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
            @RequestParam(defaultValue = "ALL", required = false) StateDto state) {

        Collection<BookingDto> bookings = bookingService.findByOwnerId(userId, state);

        log.info("GET/bookings/owner from User {} with state {} with response {}", userId, state, bookings);

        return new ResponseEntity<>(
                bookings,
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookingDto> create(@RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
                                             @Valid @RequestBody BookingDto bookingDto) {

        BookingDto booking = bookingService.create(userId, bookingDto);

        log.info("POST/bookings from User {} with body {}", userId, booking);

        return new ResponseEntity<>(
                booking,
                HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approve(@RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
                                              @RequestParam(required = true) boolean approved,
                                              @PathVariable long bookingId) {
        BookingDto booking = bookingService.approve(userId, bookingId, approved);

        log.info("PATCH/bookings/{} from User {} with state {}", bookingId, userId, approved);

        return new ResponseEntity<>(
                booking,
                HttpStatus.OK);
    }
}
