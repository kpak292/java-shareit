package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.State;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {
    Collection<Booking> getAllByBooker(User booker);

    Collection<Booking> getAllByBookerAndStatus(User booker, State state);

    Collection<Booking> getAllByBookerAndStatusAndStartBeforeAndEndAfter(User booker, State state,
                                                                         LocalDateTime start, LocalDateTime end);

    Collection<Booking> getAllByBookerAndStatusAndEndBefore(User booker, State state, LocalDateTime end);

    Collection<Booking> getAllByBookerAndStatusAndStartAfter(User booker, State state, LocalDateTime start);

    Collection<Booking> getAllByBookerAndItemAndStatusAndEndBefore(User booker,
                                                                   Item item,
                                                                   State state,
                                                                   LocalDateTime end);

    Collection<Booking> getAllByItemAndStatusAndEndBefore(Item item, State state, LocalDateTime localDateTime);

    Collection<Booking> getAllByItemAndStatusAndStartAfter(Item item, State state, LocalDateTime localDateTime);
}
