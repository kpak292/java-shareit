package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StateDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.util.Collection;

public interface BookingService {
    Collection<BookingDto> findByUserId(long userId, StateDto state);

    Collection<BookingDto> findByOwnerId(long userId, StateDto state);

    BookingDto findById(long userId, long bookingId);

    BookingDto create(long userId, BookingDto bookingDto);

    BookingDto approve(long userId, long bookingId, boolean approved);

    Collection<BookingDto> findByItemAndUser(User user, Item item);

    BookingDto findLastBooking(Item item);

    BookingDto findNextBooking(Item item);
}
