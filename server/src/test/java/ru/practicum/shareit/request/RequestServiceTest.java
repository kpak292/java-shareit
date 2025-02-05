package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestItemsDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.entity.RequestItems;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.repository.RequestItemsRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestServiceTest {
    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Mock
    ItemRequestRepository itemRequestRepository;

    @Mock
    RequestItemsRepository requestItemsRepository;

    @Mock
    UserService userService;

    User host;
    ItemRequestDto itemRequestDto;
    RequestItems requestItems;
    Item item;
    ItemRequest itemRequest;


    @BeforeEach
    public void dataPreparation() {
        host = User.builder()
                .id(1L)
                .email("test@test.uz")
                .name("host")
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .description("desc")
                .build();

        itemRequest = ItemRequest.builder()
                .id(5L)
                .description("desc")
                .build();

        item = Item.builder()
                .id(3L)
                .name("testItem")
                .host(host)
                .build();

        requestItems = RequestItems.builder()
                .id(1L)
                .item(item)
                .request(itemRequest)
                .created(LocalDateTime.now())
                .build();
    }

    //create Tests
    @Test
    public void createTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);

        when(itemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        ItemRequestDto result = itemRequestService.create(itemRequestDto, 1L);

        assertEquals(itemRequestDto.getDescription(), result.getDescription());
        assertEquals(host.getId(), result.getUserId());
        assertNotNull(result.getId());
        assertNotNull(result.getCreated());


        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(itemRequestRepository, Mockito.times(1))
                .save(Mockito.any(ItemRequest.class));
    }

    @Test
    public void createNoUserTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> itemRequestService.create(itemRequestDto, 1L));

        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
    }

    //findAllByUserId
    @Test
    public void findAllByUserIdTest() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);

        when(itemRequestRepository.findByUserId(Mockito.anyLong()))
                .thenReturn(List.of(ItemRequestMapper.INSTANCE.getItemRequest(itemRequestDto)));

        Collection<ItemRequestDto> result = itemRequestService.findAllByUserId(1L);

        itemRequestDto.setItems(List.of());
        assertTrue(result.contains(itemRequestDto));
        assertEquals(1, result.size());


        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(itemRequestRepository, Mockito.times(1))
                .findByUserId(Mockito.anyLong());
    }

    //findAll
    @Test
    public void findAll() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);

        when(itemRequestRepository.findByUserIdNot(Mockito.anyLong()))
                .thenReturn(List.of(ItemRequestMapper.INSTANCE.getItemRequest(itemRequestDto)));

        Collection<ItemRequestDto> result = itemRequestService.findAll(1L);

        assertTrue(result.contains(itemRequestDto));
        assertEquals(1, result.size());


        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(itemRequestRepository, Mockito.times(1))
                .findByUserIdNot(Mockito.anyLong());
    }

    //findById
    @Test
    public void findById() {
        //returning host on user check
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(host);

        when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(ItemRequestMapper.INSTANCE.getItemRequest(itemRequestDto)));

        ItemRequestDto result = itemRequestService.findById(1L, 2L);
        itemRequestDto.setItems(List.of());

        assertEquals(itemRequestDto, result);

        verify(userService, Mockito.times(1)).findUserById(Mockito.anyLong());
        verify(itemRequestRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    //create RequestItemsDto
    @Test
    public void create2Test() {
        when(requestItemsRepository.save(Mockito.any(RequestItems.class)))
                .thenReturn(requestItems);

        RequestItemsDto result = itemRequestService.create(item, itemRequest);

        assertEquals(item.getId(), result.getItemId());
        assertEquals(itemRequest.getId(), result.getRequestId());

        verify(requestItemsRepository, Mockito.times(1))
                .save(Mockito.any(RequestItems.class));
    }

    //create getbyId
    @Test
    public void getbyIdTest() {
        when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest));

        ItemRequest result = itemRequestService.getbyId(1L);

        assertEquals(itemRequest, result);

        verify(itemRequestRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test
    public void getbyIdEmptyTest() {
        when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemRequestService.getbyId(1L));

        verify(itemRequestRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
    }
}