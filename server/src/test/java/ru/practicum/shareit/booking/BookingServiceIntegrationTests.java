package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.StateDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceIntegrationTests {

    final BookingService bookingService;
    final UserService userService;
    final ItemService itemService;

    UserDto host;
    UserDto notHost;
    UserDto booker;
    ItemDto hostedItemAvailable;
    BookingDto waitingBooking;
    BookingDto rejectedBooking;
    BookingDto currentBooking;
    BookingDto pastBooking;
    BookingDto futureBooking;

    @BeforeEach
    public void dataPreparation() {
        host = UserDto.builder()
                .email("test@test.uz")
                .name("host")
                .build();
        host = userService.create(host);

        notHost = UserDto.builder()
                .email("test2@test.uz")
                .name("notHost")
                .build();
        notHost = userService.create(notHost);

        booker = UserDto.builder()
                .email("test3@test.uz")
                .name("booker")
                .build();
        booker = userService.create(booker);

        hostedItemAvailable = ItemDto.builder()
                .name("hostedItem")
                .description("item hosted by user #1")
                .available(true)
                .build();
        hostedItemAvailable = itemService.create(hostedItemAvailable, host.getId());

        waitingBooking = BookingDto.builder()
                .itemId(hostedItemAvailable.getId())
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().minusDays(1).plusHours(1))
                .build();
        waitingBooking = bookingService.create(booker.getId(), waitingBooking);

        rejectedBooking = BookingDto.builder()
                .itemId(hostedItemAvailable.getId())
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(2).plusHours(1))
                .build();
        rejectedBooking = bookingService.create(booker.getId(), rejectedBooking);
        rejectedBooking = bookingService.approve(host.getId(), rejectedBooking.getId(), false);

        currentBooking = BookingDto.builder()
                .itemId(hostedItemAvailable.getId())
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();
        currentBooking = bookingService.create(booker.getId(), currentBooking);
        currentBooking = bookingService.approve(host.getId(), currentBooking.getId(), true);

        pastBooking = BookingDto.builder()
                .itemId(hostedItemAvailable.getId())
                .start(LocalDateTime.now().minusDays(4))
                .end(LocalDateTime.now().minusDays(3))
                .build();
        pastBooking = bookingService.create(booker.getId(), pastBooking);
        pastBooking = bookingService.approve(host.getId(), pastBooking.getId(), true);

        futureBooking = BookingDto.builder()
                .itemId(hostedItemAvailable.getId())
                .start(LocalDateTime.now().plusDays(3))
                .end(LocalDateTime.now().plusDays(4))
                .build();
        futureBooking = bookingService.create(booker.getId(), futureBooking);
        futureBooking = bookingService.approve(host.getId(), futureBooking.getId(), true);
    }

    @Test
    public void findByUserIdWaitingTests() {
        Collection<BookingDto> bookingDtos = bookingService.findByUserId(booker.getId(), StateDto.WAITING);

        assertEquals(1, bookingDtos.size());
        assertTrue(bookingDtos.contains(waitingBooking));
    }

    @Test
    public void findByUserIdRejectedTests() {
        Collection<BookingDto> bookingDtos = bookingService.findByUserId(booker.getId(), StateDto.REJECTED);

        assertEquals(1, bookingDtos.size());
        assertTrue(bookingDtos.contains(rejectedBooking));
    }

    @Test
    public void findByUserIdCurrentTests() {
        Collection<BookingDto> bookingDtos = bookingService.findByUserId(booker.getId(), StateDto.CURRENT);

        assertEquals(1, bookingDtos.size());
        assertTrue(bookingDtos.contains(currentBooking));
    }

    @Test
    public void findByUserIdPastTests() {
        Collection<BookingDto> bookingDtos = bookingService.findByUserId(booker.getId(), StateDto.PAST);

        assertEquals(1, bookingDtos.size());
        assertTrue(bookingDtos.contains(pastBooking));
    }

    @Test
    public void findByUserIdFutureTests() {
        Collection<BookingDto> bookingDtos = bookingService.findByUserId(booker.getId(), StateDto.FUTURE);

        assertEquals(1, bookingDtos.size());
        assertTrue(bookingDtos.contains(futureBooking));
    }

    @Test
    public void findByUserIdAllTests() {
        Collection<BookingDto> bookingDtos = bookingService.findByUserId(booker.getId(), StateDto.ALL);

        assertEquals(5, bookingDtos.size());
        assertTrue(bookingDtos.contains(futureBooking));
        assertTrue(bookingDtos.contains(waitingBooking));
        assertTrue(bookingDtos.contains(rejectedBooking));
        assertTrue(bookingDtos.contains(pastBooking));
        assertTrue(bookingDtos.contains(currentBooking));
    }

    @Test
    public void findByOwnerIdAllTests() {
        Collection<BookingDto> bookingDtos = bookingService.findByOwnerId(host.getId(), StateDto.ALL);

        assertEquals(5, bookingDtos.size());
        assertTrue(bookingDtos.contains(futureBooking));
        assertTrue(bookingDtos.contains(waitingBooking));
        assertTrue(bookingDtos.contains(rejectedBooking));
        assertTrue(bookingDtos.contains(pastBooking));
        assertTrue(bookingDtos.contains(currentBooking));
    }

    @Test
    public void findByOwnerIdWaitingTests() {
        Collection<BookingDto> bookingDtos = bookingService.findByOwnerId(host.getId(), StateDto.WAITING);

        assertEquals(1, bookingDtos.size());
        assertTrue(bookingDtos.contains(waitingBooking));
    }

    @Test
    public void findByOwnerIdRejectedTests() {
        Collection<BookingDto> bookingDtos = bookingService.findByOwnerId(host.getId(), StateDto.REJECTED);

        assertEquals(1, bookingDtos.size());
        assertTrue(bookingDtos.contains(rejectedBooking));
    }

    @Test
    public void findByOwnerIdCurrentTests() {
        Collection<BookingDto> bookingDtos = bookingService.findByOwnerId(host.getId(), StateDto.CURRENT);

        assertEquals(1, bookingDtos.size());
        assertTrue(bookingDtos.contains(currentBooking));
    }

    @Test
    public void findByOwnerIdPastTests() {
        Collection<BookingDto> bookingDtos = bookingService.findByOwnerId(host.getId(), StateDto.PAST);

        assertEquals(1, bookingDtos.size());
        assertTrue(bookingDtos.contains(pastBooking));
    }

    @Test
    public void findByOwnerIdFutureTests() {
        Collection<BookingDto> bookingDtos = bookingService.findByOwnerId(host.getId(), StateDto.FUTURE);

        assertEquals(1, bookingDtos.size());
        assertTrue(bookingDtos.contains(futureBooking));
    }

    @Test
    public void findByItemAndUser() {
        Collection<BookingDto> bookingDtos = bookingService.findByItemAndUser(
                UserMapper.INSTANCE.getUser(booker), ItemMapper.INSTANCE.getItem(hostedItemAvailable));

        assertEquals(1, bookingDtos.size());
        assertTrue(bookingDtos.contains(pastBooking));
    }

    @Test
    public void findLastBooking() {
        Optional<Booking> lastBooking = bookingService.findLastBooking(
                ItemMapper.INSTANCE.getItem(hostedItemAvailable));

        assertTrue(lastBooking.isPresent());
        Booking expected = BookingMapper.INSTANCE.getBooking(pastBooking);
        expected.getItem().setHost(UserMapper.INSTANCE.getUser(host));

        assertEquals(expected, lastBooking.get());
    }

    @Test
    public void findNextBooking() {
        Optional<Booking> nextBooking = bookingService.findNextBooking(
                ItemMapper.INSTANCE.getItem(hostedItemAvailable));

        assertTrue(nextBooking.isPresent());
        Booking expected = BookingMapper.INSTANCE.getBooking(futureBooking);
        expected.getItem().setHost(UserMapper.INSTANCE.getUser(host));

        assertEquals(expected, nextBooking.get());
    }
}
