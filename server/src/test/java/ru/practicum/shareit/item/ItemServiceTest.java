package ru.practicum.shareit.item;

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
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
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
public class ItemServiceTest {
    @InjectMocks
    ItemServiceImpl itemService;

    @Mock
    ItemRepository itemRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserService userService;

    @Mock
    BookingService bookingService;

    @Mock
    ItemRequestService itemRequestService;

    User host;
    Item itemAvailable;
    Item itemNotAvailable;
    ItemRequest itemRequest;
    BookingDto bookingDto;
    CommentDto commentDto;

    @BeforeEach
    public void dataPreparation() {
        LocalDateTime commentTime = LocalDateTime.now();
        host = User.builder()
                .id(1L)
                .email("test@test.uz")
                .name("host")
                .build();

        itemAvailable = Item.builder()
                .id(1L)
                .host(host)
                .name("hostedItem")
                .description("item hosted by user #1")
                .available(true)
                .build();

        itemNotAvailable = Item.builder()
                .id(2L)
                .host(host)
                .name("hostedItem")
                .description("item hosted by user #1 not available")
                .available(false)
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("testdesc")
                .created(LocalDateTime.now())
                .userId(1L)
                .build();

        bookingDto = BookingDto.builder()
                .id(1L)
                .itemId(1L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().minusDays(1).plusHours(1))
                .build();

        commentDto = CommentDto.builder()
                .id(2L)
                .text("CommentText")
                .authorName("host")
                .created(commentTime)
                .build();
    }

    //findAllByUser Tests
    @Test
    public void findAllByUserTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);
        //returning list on any request
        when(itemRepository.findByHost(Mockito.any(User.class)))
                .thenReturn(List.of(itemAvailable));

        when(commentRepository.findAllByItem(Mockito.any(Item.class)))
                .thenReturn(List.of());

        when(bookingService.findLastBooking(Mockito.any(Item.class)))
                .thenReturn(Optional.empty());

        when(bookingService.findNextBooking(Mockito.any(Item.class)))
                .thenReturn(Optional.empty());

        Collection<ItemDto> requestResult = itemService.findAllByUser(1L);

        ItemDto itemDto = ItemMapper.INSTANCE.getItemDto(itemAvailable);
        itemDto.setComments(List.of());
        List<ItemDto> expected = List.of(itemDto);

        assertArrayEquals(expected.toArray(), requestResult.toArray());

        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(itemRepository, Mockito.times(1))
                .findByHost(Mockito.any(User.class));
        verify(commentRepository, Mockito.times(1))
                .findAllByItem(Mockito.any(Item.class));
        verify(bookingService, Mockito.times(1))
                .findLastBooking(Mockito.any(Item.class));
        verify(bookingService, Mockito.times(1))
                .findNextBooking(Mockito.any(Item.class));
    }

    @Test
    public void findAllByIncorrectUserTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> itemService.findAllByUser(1L));

        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verifyNoInteractions(itemRepository);
    }

    @Test
    public void findNothingByUserTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);
        //returning list on any request
        when(itemRepository.findByHost(Mockito.any(User.class)))
                .thenReturn(List.of());

        Collection<ItemDto> requestResult = itemService.findAllByUser(1L);

        List<ItemDto> expected = List.of();

        assertArrayEquals(expected.toArray(), requestResult.toArray());

        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(itemRepository, Mockito.times(1))
                .findByHost(Mockito.any(User.class));
        verifyNoInteractions(commentRepository);
        verifyNoInteractions(bookingService);
    }

    //findById Tests
    @Test
    public void findByIdTest() {
        //returning list on any request
        when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemAvailable));

        when(commentRepository.findAllByItem(Mockito.any(Item.class)))
                .thenReturn(List.of());

        when(bookingService.findLastBooking(Mockito.any(Item.class)))
                .thenReturn(Optional.empty());

        when(bookingService.findNextBooking(Mockito.any(Item.class)))
                .thenReturn(Optional.empty());

        ItemDto requestResult = itemService.findById(1L, 7L);

        ItemDto itemDto = ItemMapper.INSTANCE.getItemDto(itemAvailable);
        itemDto.setComments(List.of());

        assertEquals(itemDto, requestResult);

        verify(itemRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
        verify(commentRepository, Mockito.times(1))
                .findAllByItem(Mockito.any(Item.class));
        verify(bookingService, Mockito.times(1))
                .findLastBooking(Mockito.any(Item.class));
        verify(bookingService, Mockito.times(1))
                .findNextBooking(Mockito.any(Item.class));
    }

    @Test
    public void findNothingByIdTest() {
        //returning list on any request
        when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.findById(1L, 7L));

        verify(itemRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
        verifyNoInteractions(commentRepository);
        verifyNoInteractions(bookingService);
    }

    //findByText Tests - need to be covered by integration tests additionally
    @Test
    public void findByTextTest() {
        when(itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(List.of(itemAvailable, itemNotAvailable));

        ItemDto itemDto = ItemMapper.INSTANCE.getItemDto(itemAvailable);

        Collection<ItemDto> requestResult = itemService.findByText("test");

        assertArrayEquals(List.of(itemDto).toArray(), requestResult.toArray());

        verify(itemRepository, Mockito.times(1))
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(Mockito.anyString(), Mockito.anyString());
    }

    //create tests
    @Test
    public void createTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);

        when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(itemAvailable);

        ItemDto itemDto = ItemMapper.INSTANCE.getItemDto(itemAvailable);

        ItemDto requestResult = itemService.create(itemDto, host.getId());

        assertEquals(itemDto, requestResult);

        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(itemRepository, Mockito.times(1))
                .save(Mockito.any(Item.class));
    }

    @Test
    public void createIncorrectUserTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenThrow(NotFoundException.class);

        ItemDto itemDto = ItemMapper.INSTANCE.getItemDto(itemAvailable);

        assertThrows(NotFoundException.class,
                () -> itemService.create(itemDto, host.getId()));

        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verifyNoInteractions(itemRepository);
    }

    @Test
    public void createWithRequestTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);

        when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(itemAvailable);

        when(itemRequestService.getbyId(Mockito.anyLong()))
                .thenReturn(itemRequest);

        ItemDto itemDto = ItemMapper.INSTANCE.getItemDto(itemAvailable);
        itemDto.setRequestId(1L);

        ItemDto requestResult = itemService.create(itemDto, host.getId());

        itemDto.setRequestId(null);
        assertEquals(itemDto, requestResult);

        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(itemRepository, Mockito.times(1))
                .save(Mockito.any(Item.class));
        verify(itemRequestService, Mockito.times(1))
                .getbyId(Mockito.anyLong());
        verify(itemRequestService, Mockito.times(1))
                .create(Mockito.any(Item.class), Mockito.any(ItemRequest.class));
    }

    //update tests
    @Test
    public void updateTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);

        when(itemRepository.findByIdAndHost(Mockito.anyLong(), Mockito.any(User.class)))
                .thenReturn(Optional.of(itemAvailable));

        when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(itemAvailable);

        ItemDto itemDto = ItemMapper.INSTANCE.getItemDto(itemAvailable);
        itemDto.setDescription("changed");
        itemDto.setName("changed");
        itemDto.setAvailable(false);

        ItemDto requestResult = itemService.update(1L, itemDto, host.getId());

        assertEquals(itemDto, requestResult);

        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(itemRepository, Mockito.times(1))
                .findByIdAndHost(Mockito.anyLong(), Mockito.any(User.class));
        verify(itemRepository, Mockito.times(1))
                .save(Mockito.any(Item.class));
    }

    //remove tests
    @Test
    public void removeTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);

        when(itemRepository.findByIdAndHost(Mockito.anyLong(), Mockito.any(User.class)))
                .thenReturn(Optional.of(itemAvailable));

        ItemDto itemDto = ItemMapper.INSTANCE.getItemDto(itemAvailable);

        ItemDto requestResult = itemService.remove(1L, 2L);

        assertEquals(itemDto, requestResult);

        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(itemRepository, Mockito.times(1))
                .findByIdAndHost(Mockito.anyLong(), Mockito.any(User.class));
        verify(itemRepository, Mockito.times(1))
                .deleteById(Mockito.anyLong());
    }

    @Test
    public void removeNothingTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);

        when(itemRepository.findByIdAndHost(Mockito.anyLong(), Mockito.any(User.class)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.remove(1L, 2L));

        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(itemRepository, Mockito.times(1))
                .findByIdAndHost(Mockito.anyLong(), Mockito.any(User.class));
    }

    //addComment Test
    @Test
    public void addCommentTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);

        when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemAvailable));

        when(bookingService.findByItemAndUser(Mockito.any(User.class), Mockito.any(Item.class)))
                .thenReturn(List.of(bookingDto));

        when(commentRepository.save(Mockito.any(Comment.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        CommentDto result = itemService.addComment(1L, commentDto, 2L);
        result.setCreated(commentDto.getCreated());

        assertEquals(commentDto, result);

        verify(userService, Mockito.times(1))
                .findUserById(Mockito.anyLong());
        verify(itemRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
        verify(bookingService, Mockito.times(1))
                .findByItemAndUser(Mockito.any(User.class), Mockito.any(Item.class));
        verify(commentRepository, Mockito.times(1))
                .save(Mockito.any(Comment.class));
    }

    @Test
    public void addCommentNoItemTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);

        when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.addComment(1L, commentDto, 2L));

        verify(userService, Mockito.times(1))
                .findUserById(Mockito.anyLong());
        verify(itemRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test
    public void addCommentNoBookingsTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);

        when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemAvailable));

        when(bookingService.findByItemAndUser(Mockito.any(User.class), Mockito.any(Item.class)))
                .thenReturn(List.of());

        assertThrows(NotAvailableException.class,
                () -> itemService.addComment(1L, commentDto, 2L));

        verify(userService, Mockito.times(1))
                .findUserById(Mockito.anyLong());
        verify(itemRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
        verify(bookingService, Mockito.times(1))
                .findByItemAndUser(Mockito.any(User.class), Mockito.any(Item.class));
    }
}