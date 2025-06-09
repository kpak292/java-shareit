package ru.practicum.shareit.booking;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.StateDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnauthorizedAccessException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceTest {
    @InjectMocks
    BookingServiceImpl bookingService;

    @Mock
    BookingRepository mockBookingrepository;

    @Mock
    UserService mockUserService;

    @Mock
    ItemService mockItemService;


    User host;
    User notHost;
    User booker;
    BookingDto requstBooking;
    Booking waitingBooking;
    Booking approvedBooking;
    Item hostedItemAvailable;
    Item notHostedItemAvailable;
    Item hostedItemNotAvailable;

    @BeforeEach
    public void dataPreparation() {
        host = User.builder()
                .id(1L)
                .email("test@test.uz")
                .name("host")
                .build();

        notHost = User.builder()
                .id(2L)
                .email("test2@test.uz")
                .name("notHost")
                .build();

        booker = User.builder()
                .id(3L)
                .email("test3@test.uz")
                .name("booker")
                .build();

        hostedItemAvailable = Item.builder()
                .id(1L)
                .host(host)
                .name("hostedItem")
                .description("item hosted by user #1")
                .available(true)
                .build();

        notHostedItemAvailable = Item.builder()
                .id(2L)
                .host(notHost)
                .name("not hostedItem")
                .description("item not hosted by user #1")
                .available(true)
                .build();

        hostedItemNotAvailable = Item.builder()
                .id(3L)
                .host(host)
                .name("hostedItem")
                .description("item hosted by user #1 not available")
                .available(false)
                .build();

        waitingBooking = Booking.builder()
                .id(1L)
                .booker(booker)
                .item(hostedItemAvailable)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now())
                .status(State.WAITING)
                .build();

        requstBooking = BookingDto.builder()
                .id(1L)
                .itemId(1L)
                .start(waitingBooking.getStart())
                .end(waitingBooking.getEnd())
                .build();

        approvedBooking = Booking.builder()
                .id(2L)
                .booker(booker)
                .item(notHostedItemAvailable)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now())
                .status(State.APPROVED)
                .build();

    }

    //findByUserId - need to be covered by integration tests additionally
    @Test
    public void findAllByUserIdTest() {
        //returning host on user check
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenReturn(host);
        //returning list on any request
        when(mockBookingrepository.getAllByBooker(Mockito.any(User.class)))
                .thenReturn(List.of(waitingBooking, approvedBooking));

        Collection<BookingDto> requestResult = bookingService.findByUserId(1L, StateDto.ALL);

        List<BookingDto> expected = List.of(BookingMapper.INSTANCE.getBookingDto(waitingBooking),
                BookingMapper.INSTANCE.getBookingDto(approvedBooking));

        assertArrayEquals(expected.toArray(), requestResult.toArray());

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(mockBookingrepository, Mockito.times(1))
                .getAllByBooker(Mockito.any(User.class));
    }

    @Test
    public void findWaitingByUserIdTest() {
        //returning host on user check
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenReturn(host);
        //returning list on any request
        when(mockBookingrepository.getAllByBookerAndStatus(Mockito.any(User.class), Mockito.any(State.class)))
                .thenReturn(List.of(waitingBooking, approvedBooking));

        Collection<BookingDto> requestResult = bookingService.findByUserId(1L, StateDto.WAITING);

        List<BookingDto> expected = List.of(BookingMapper.INSTANCE.getBookingDto(waitingBooking),
                BookingMapper.INSTANCE.getBookingDto(approvedBooking));

        assertArrayEquals(expected.toArray(), requestResult.toArray());

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(mockBookingrepository, Mockito.times(1))
                .getAllByBookerAndStatus(Mockito.any(User.class), Mockito.any(State.class));
    }

    @Test
    public void findRejectedByUserIdTest() {
        //returning host on user check
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenReturn(host);
        //returning list on any request
        when(mockBookingrepository.getAllByBookerAndStatus(Mockito.any(User.class), Mockito.any(State.class)))
                .thenReturn(List.of(waitingBooking, approvedBooking));

        Collection<BookingDto> requestResult = bookingService.findByUserId(1L, StateDto.REJECTED);

        List<BookingDto> expected = List.of(BookingMapper.INSTANCE.getBookingDto(waitingBooking),
                BookingMapper.INSTANCE.getBookingDto(approvedBooking));

        assertArrayEquals(expected.toArray(), requestResult.toArray());

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(mockBookingrepository, Mockito.times(1))
                .getAllByBookerAndStatus(Mockito.any(User.class), Mockito.any(State.class));
    }

    @Test
    public void findCurrentByUserIdTest() {
        //returning host on user check
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenReturn(host);
        //returning list on any request
        when(mockBookingrepository.getAllByBookerAndStatusAndStartBeforeAndEndAfter(Mockito.any(User.class),
                Mockito.any(State.class), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of(waitingBooking, approvedBooking));

        Collection<BookingDto> requestResult = bookingService.findByUserId(1L, StateDto.CURRENT);

        List<BookingDto> expected = List.of(BookingMapper.INSTANCE.getBookingDto(waitingBooking),
                BookingMapper.INSTANCE.getBookingDto(approvedBooking));

        assertArrayEquals(expected.toArray(), requestResult.toArray());

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(mockBookingrepository, Mockito.times(1))
                .getAllByBookerAndStatusAndStartBeforeAndEndAfter(Mockito.any(User.class),
                        Mockito.any(State.class), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class));
    }

    @Test
    public void findPastByUserIdTest() {
        //returning host on user check
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenReturn(host);
        //returning list on any request
        when(mockBookingrepository.getAllByBookerAndStatusAndEndBefore(Mockito.any(User.class),
                Mockito.any(State.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of(waitingBooking, approvedBooking));

        Collection<BookingDto> requestResult = bookingService.findByUserId(1L, StateDto.PAST);

        List<BookingDto> expected = List.of(BookingMapper.INSTANCE.getBookingDto(waitingBooking),
                BookingMapper.INSTANCE.getBookingDto(approvedBooking));

        assertArrayEquals(expected.toArray(), requestResult.toArray());

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(mockBookingrepository, Mockito.times(1))
                .getAllByBookerAndStatusAndEndBefore(Mockito.any(User.class),
                        Mockito.any(State.class), Mockito.any(LocalDateTime.class));
    }

    @Test
    public void findFutureByUserIdTest() {
        //returning host on user check
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenReturn(host);
        //returning list on any request
        when(mockBookingrepository.getAllByBookerAndStatusAndStartAfter(Mockito.any(User.class),
                Mockito.any(State.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of(waitingBooking, approvedBooking));

        Collection<BookingDto> requestResult = bookingService.findByUserId(1L, StateDto.FUTURE);

        List<BookingDto> expected = List.of(BookingMapper.INSTANCE.getBookingDto(waitingBooking),
                BookingMapper.INSTANCE.getBookingDto(approvedBooking));

        assertArrayEquals(expected.toArray(), requestResult.toArray());

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(mockBookingrepository, Mockito.times(1))
                .getAllByBookerAndStatusAndStartAfter(Mockito.any(User.class),
                        Mockito.any(State.class), Mockito.any(LocalDateTime.class));
    }

    @Test
    public void findByIncorrectUserIdTest() {
        //returning host on user check
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenThrow(new NotFoundException("User is not found"));

        assertThrows(NotFoundException.class,
                () -> bookingService.findByUserId(1L, StateDto.ALL));

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verifyNoInteractions(mockBookingrepository);
    }

    //findByOwnerId - need to be covered by integration tests additionally
    @Test
    public void findByOwnerIdTest() {
        //returning host on user check
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenReturn(host);
        //returning list on any request
        when(mockBookingrepository.findAll(Mockito.any(BooleanExpression.class)))
                .thenReturn(List.of(waitingBooking, approvedBooking));

        Collection<BookingDto> requestResult = bookingService.findByOwnerId(1L, StateDto.WAITING);

        List<BookingDto> expected = List.of(BookingMapper.INSTANCE.getBookingDto(waitingBooking),
                BookingMapper.INSTANCE.getBookingDto(approvedBooking));

        assertArrayEquals(expected.toArray(), requestResult.toArray());

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(mockBookingrepository, Mockito.times(1))
                .findAll(Mockito.any(BooleanExpression.class));
    }

    @Test
    public void findByIncorrectOwnerIdTest() {
        //returning host on user check
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenThrow(new NotFoundException("User is not found"));

        assertThrows(NotFoundException.class,
                () -> bookingService.findByOwnerId(1L, StateDto.ALL));

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verifyNoInteractions(mockBookingrepository);
    }

    //findById Tests

    @Test
    public void findByIdTest() {
        when(mockBookingrepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(waitingBooking));

        BookingDto result = bookingService.findById(3L, 1L);

        assertEquals(BookingMapper.INSTANCE.getBookingDto(waitingBooking), result);

        verify(mockBookingrepository, Mockito.times(1)).findById(Mockito.anyLong());
        verifyNoMoreInteractions(mockBookingrepository);
    }

    @Test
    public void findByIdNotAvailableTest() {
        when(mockBookingrepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.findById(3L, 1L));

        verify(mockBookingrepository, Mockito.times(1)).findById(Mockito.anyLong());
        verifyNoMoreInteractions(mockBookingrepository);
    }

    @Test
    public void findIncorrectUserByIdTest() {
        when(mockBookingrepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(waitingBooking));

        assertThrows(UnauthorizedAccessException.class,
                () -> bookingService.findById(2L, 1L));

        verify(mockBookingrepository, Mockito.times(1)).findById(Mockito.anyLong());
        verifyNoMoreInteractions(mockBookingrepository);
    }

    //create tests

    @Test
    public void createTest() {
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenReturn(booker);

        when(mockItemService.findItemById(Mockito.anyLong()))
                .thenReturn(hostedItemAvailable);

        when(mockBookingrepository.save(Mockito.any(Booking.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        BookingDto result = bookingService.create(1L, requstBooking);

        assertEquals(result, BookingMapper.INSTANCE.getBookingDto(waitingBooking));

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(mockItemService, Mockito.times(1)).findItemById(Mockito.anyLong());
        verify(mockBookingrepository, Mockito.times(1)).save(Mockito.any(Booking.class));
        verifyNoMoreInteractions(mockBookingrepository);
        verifyNoMoreInteractions(mockItemService);
        verifyNoMoreInteractions(mockUserService);
    }

    @Test
    public void createUnknownUserTest() {
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> bookingService.create(1L, requstBooking));

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verifyNoInteractions(mockBookingrepository);
        verifyNoInteractions(mockItemService);
        verifyNoMoreInteractions(mockUserService);
    }

    @Test
    public void createUnknownItemTest() {
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenReturn(booker);

        when(mockItemService.findItemById(Mockito.anyLong()))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> bookingService.create(1L, requstBooking));

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(mockItemService, Mockito.times(1)).findItemById(Mockito.anyLong());
        verifyNoInteractions(mockBookingrepository);
        verifyNoMoreInteractions(mockItemService);
        verifyNoMoreInteractions(mockUserService);
    }

    @Test
    public void createNotAvailableItemTest() {
        when(mockUserService.findUserById(Mockito.anyLong()))
                .thenReturn(booker);

        when(mockItemService.findItemById(Mockito.anyLong()))
                .thenReturn(hostedItemNotAvailable);

        assertThrows(NotAvailableException.class,
                () -> bookingService.create(1L, requstBooking));

        verify(mockUserService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(mockItemService, Mockito.times(1)).findItemById(Mockito.anyLong());
        verifyNoInteractions(mockBookingrepository);
        verifyNoMoreInteractions(mockItemService);
        verifyNoMoreInteractions(mockUserService);
    }

    //approve tests

    @Test
    public void approveTest() {
        when(mockBookingrepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(waitingBooking));

        when(mockBookingrepository.save(Mockito.any(Booking.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        BookingDto result = bookingService.approve(1L, 1L, true);

        assertEquals(State.APPROVED, result.getStatus());

        verify(mockBookingrepository, Mockito.times(1)).findById(Mockito.anyLong());
        verify(mockBookingrepository, Mockito.times(1)).save(Mockito.any(Booking.class));
        verifyNoMoreInteractions(mockBookingrepository);
    }

    @Test
    public void notApproveTest() {
        when(mockBookingrepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(waitingBooking));

        when(mockBookingrepository.save(Mockito.any(Booking.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        BookingDto result = bookingService.approve(1L, 1L, false);

        assertEquals(State.REJECTED, result.getStatus());

        verify(mockBookingrepository, Mockito.times(1)).findById(Mockito.anyLong());
        verify(mockBookingrepository, Mockito.times(1)).save(Mockito.any(Booking.class));
        verifyNoMoreInteractions(mockBookingrepository);
    }

    @Test
    public void approveNotExistingBookingTest() {
        when(mockBookingrepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.approve(1L, 1L, true));

        verify(mockBookingrepository, Mockito.times(1)).findById(Mockito.anyLong());
        verifyNoMoreInteractions(mockBookingrepository);
    }

    @Test
    public void approveNotBookerTest() {
        when(mockBookingrepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(waitingBooking));

        assertThrows(UnauthorizedAccessException.class,
                () -> bookingService.approve(2L, 1L, true));

        verify(mockBookingrepository, Mockito.times(1)).findById(Mockito.anyLong());
        verifyNoMoreInteractions(mockBookingrepository);
    }

    //findByItemAndUser tests - need to be covered by integration tests additionally

    @Test
    public void findByItemAndUserTest() {
        when(mockBookingrepository.getAllByBookerAndItemAndStatusAndEndBefore(any(User.class),
                any(Item.class),
                any(State.class),
                any(LocalDateTime.class)))
                .thenReturn(List.of(waitingBooking, approvedBooking));

        Collection<BookingDto> result = bookingService.findByItemAndUser(booker, hostedItemAvailable);

        assertArrayEquals(List.of(BookingMapper.INSTANCE.getBookingDto(waitingBooking),
                        BookingMapper.INSTANCE.getBookingDto(approvedBooking)).toArray(),
                result.toArray());
        verify(mockBookingrepository, Mockito.times(1))
                .getAllByBookerAndItemAndStatusAndEndBefore(any(User.class),
                        any(Item.class),
                        any(State.class),
                        any(LocalDateTime.class));
        verifyNoMoreInteractions(mockBookingrepository);
    }

    //findByLastBooking tests - need to be covered by integration tests additionally
    @Test
    public void findByLastBookingTest() {
        when(mockBookingrepository.findFirstByItemAndStatusAndEndBeforeOrderByEndDesc(any(Item.class),
                any(State.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        bookingService.findLastBooking(hostedItemAvailable);

        verify(mockBookingrepository, Mockito.times(1))
                .findFirstByItemAndStatusAndEndBeforeOrderByEndDesc(
                        any(Item.class),
                        any(State.class),
                        any(LocalDateTime.class));
        verifyNoMoreInteractions(mockBookingrepository);
    }

    //findByNextBooking tests - need to be covered by integration tests additionally
    @Test
    public void findByNextBookingTest() {
        when(mockBookingrepository.findFirstByItemAndStatusAndStartAfterOrderByStartAsc(any(Item.class),
                any(State.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        bookingService.findNextBooking(hostedItemAvailable);

        verify(mockBookingrepository, Mockito.times(1))
                .findFirstByItemAndStatusAndStartAfterOrderByStartAsc(
                        any(Item.class),
                        any(State.class),
                        any(LocalDateTime.class));
        verifyNoMoreInteractions(mockBookingrepository);
    }
}
