package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.StateDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.QBooking;
import ru.practicum.shareit.booking.entity.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnauthorizedAccessException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service("bookingServiceV1")
public class BookingServiceImpl implements BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    @Qualifier("userServiceV1")
    UserService userService;

    @Autowired
    @Qualifier("itemServiceV1")
    ItemService itemService;

    @Override
    public Collection<BookingDto> findByUserId(long userId, StateDto state) {
        //Check user exists
        User booker = userService.findUserById(userId);

        Collection<Booking> result = List.of();

        switch (state) {
            case ALL -> result = bookingRepository.getAllByBooker(booker);

            case WAITING -> result = bookingRepository.getAllByBookerAndStatus(booker, State.WAITING);

            case REJECTED -> result = bookingRepository.getAllByBookerAndStatus(booker, State.REJECTED);

            case CURRENT -> result = bookingRepository.getAllByBookerAndStatusAndStartBeforeAndEndAfter(
                    booker, State.APPROVED, LocalDateTime.now(), LocalDateTime.now());

            case PAST -> result = bookingRepository.getAllByBookerAndStatusAndEndBefore(booker, State.APPROVED,
                    LocalDateTime.now());

            case FUTURE -> result = bookingRepository.getAllByBookerAndStatusAndStartAfter(booker, State.APPROVED,
                    LocalDateTime.now());
        }

        return result.stream()
                .map(BookingMapper.INSTANCE::getBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> findByOwnerId(long userId, StateDto state) {
        //Check if user exists
        User host = userService.findUserById(userId);

        Iterable<Booking> result = List.of();

        BooleanExpression byItemHost = QBooking.booking.item.host.id.eq(userId);

        switch (state) {
            case ALL -> {
                result = bookingRepository.findAll(byItemHost);
            }
            case WAITING -> {
                BooleanExpression byStatus = QBooking.booking.status.eq(State.WAITING);
                result = bookingRepository.findAll(byItemHost.and(byStatus));
            }
            case REJECTED -> {
                BooleanExpression byStatus = QBooking.booking.status.eq(State.REJECTED);
                result = bookingRepository.findAll(byItemHost.and(byStatus));
            }
            case CURRENT -> {
                BooleanExpression byStatus = QBooking.booking.status.eq(State.APPROVED);
                BooleanExpression byStart = QBooking.booking.start.before(LocalDateTime.now());
                BooleanExpression byEnd = QBooking.booking.end.after(LocalDateTime.now());
                result = bookingRepository.findAll(byItemHost.and(byStatus).and(byStart).and(byEnd));
            }
            case PAST -> {
                BooleanExpression byStatus = QBooking.booking.status.eq(State.APPROVED);
                BooleanExpression byEnd = QBooking.booking.end.before(LocalDateTime.now());
                result = bookingRepository.findAll(byItemHost.and(byStatus).and(byEnd));
            }
            case FUTURE -> {
                BooleanExpression byStatus = QBooking.booking.status.eq(State.APPROVED);
                BooleanExpression byStart = QBooking.booking.start.after(LocalDateTime.now());
                result = bookingRepository.findAll(byItemHost.and(byStatus).and(byStart));
            }
        }


        return BookingMapper.INSTANCE.mapToBookingDto(result);
    }

    @Override
    public BookingDto findById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking is not found with id = " + bookingId));

        if (booking.getBooker().getId() == userId || booking.getItem().getHost().getId() == userId) {
            return BookingMapper.INSTANCE.getBookingDto(booking);
        } else {
            throw new UnauthorizedAccessException("User has not access to this booking");
        }
    }

    @Override
    public BookingDto create(long userId, BookingDto bookingDto) {
        //Check user exists
        User user = userService.findUserById(userId);

        Booking booking = BookingMapper.INSTANCE.getBooking(bookingDto);

        Item item = itemService.findItemById(bookingDto.getItemId());

        if (!item.isAvailable()) {
            throw new NotAvailableException("Item is not available with id = " + bookingDto.getItemId());
        }

        booking.setBooker(user);
        booking.setStatus(State.WAITING);
        booking.setItem(item);

        return BookingMapper.INSTANCE.getBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approve(long userId, long bookingId, boolean approved) {
        //Check booking exists
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking is not found with id = " + bookingId));

        if (booking.getItem().getHost().getId() != userId) {
            throw new UnauthorizedAccessException("User has no rights to approve Item booking");
        }

        if (approved) {
            booking.setStatus(State.APPROVED);
        } else {
            booking.setStatus(State.REJECTED);
        }

        return BookingMapper.INSTANCE.getBookingDto(bookingRepository.save(booking));
    }

    @Override
    public Collection<BookingDto> findByItemAndUser(User user, Item item) {
        Collection<Booking> result = bookingRepository.getAllByBookerAndItemAndStatusAndEndBefore(user,
                item,
                State.APPROVED,
                LocalDateTime.now());

        return result.stream()
                .map(BookingMapper.INSTANCE::getBookingDto)
                .toList();
    }

    @Override
    public BookingDto findLastBooking(Item item) {
        //Добавил минус 1 день, тк по моей логике подтягивается бронирование по которому комментарий ставили
        // (что по сути является PAST)
        Collection<Booking> bookings = bookingRepository.getAllByItemAndStatusAndEndBefore(item, State.APPROVED,
                LocalDateTime.now().minusDays(1));

        if (bookings.isEmpty()) {
            return null;
        } else {
            return bookings.stream()
                    .map(BookingMapper.INSTANCE::getBookingDto)
                    .max(Comparator.comparing(BookingDto::getEnd))
                    .get();
        }

    }

    @Override
    public BookingDto findNextBooking(Item item) {
        Collection<Booking> bookings = bookingRepository.getAllByItemAndStatusAndStartAfter(item, State.APPROVED,
                LocalDateTime.now());

        if (bookings.isEmpty()) {
            return null;
        } else {
            return bookings.stream()
                    .map(BookingMapper.INSTANCE::getBookingDto)
                    .min(Comparator.comparing(BookingDto::getStart))
                    .get();
        }
    }
}
