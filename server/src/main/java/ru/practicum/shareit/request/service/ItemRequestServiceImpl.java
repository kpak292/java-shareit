package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestItemsDto;
import ru.practicum.shareit.request.dto.RequestItemsMapper;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.entity.RequestItems;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.repository.RequestItemsRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service("itemRequestServiceV1")
public class ItemRequestServiceImpl implements ItemRequestService {
    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Autowired
    @Qualifier("userServiceV1")
    UserService userService;

    @Autowired
    RequestItemsRepository requestItemsRepository;

    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto, long userId) {
        userService.findUserById(userId);

        ItemRequest itemRequest = ItemRequestMapper.INSTANCE.getItemRequest(itemRequestDto);
        itemRequest.setUserId(userId);
        itemRequest.setCreated(LocalDateTime.now());

        itemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.INSTANCE.getItemRequestDto(itemRequest);
    }

    @Override
    public Collection<ItemRequestDto> findAllByUserId(long userId) {
        userService.findUserById(userId);

        return itemRequestRepository.findByUserId(userId).stream()
                .map(this::loadRequestItems)
                .toList();
    }

    @Override
    public Collection<ItemRequestDto> findAll(long userId) {
        userService.findUserById(userId);

        return itemRequestRepository.findByUserIdNot(userId).stream()
                .map(ItemRequestMapper.INSTANCE::getItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto findById(long requestId, long userId) {
        userService.findUserById(userId);

        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Item request is not found with id = " + requestId));
        return loadRequestItems(itemRequest);
    }

    @Override
    public RequestItemsDto create(Item item, ItemRequest itemRequest) {
        RequestItems requestItems = RequestItems.builder()
                .item(item)
                .request(itemRequest)
                .created(LocalDateTime.now())
                .build();

        requestItems = requestItemsRepository.save(requestItems);

        return RequestItemsMapper.INSTANCE.getRequestItemsDto(requestItems);
    }

    @Override
    public ItemRequest getbyId(long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Item request is not found with id = " + requestId));
    }

    private ItemRequestDto loadRequestItems(ItemRequest itemRequest) {
        Collection<RequestItems> requestItems = requestItemsRepository.findByRequest(itemRequest);

        List<Item> items = requestItems.stream()
                .map(RequestItems::getItem)
                .toList();

        ItemRequestDto itemRequestDto = ItemRequestMapper.INSTANCE.getItemRequestDto(itemRequest);

        itemRequestDto.setItems(items.stream()
                .map(ItemMapper.INSTANCE::getItemDto)
                .toList());

        return itemRequestDto;
    }

}
