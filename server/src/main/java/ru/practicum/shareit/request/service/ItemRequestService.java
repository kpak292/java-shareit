package ru.practicum.shareit.request.service;

import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemsDto;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto, long userId);

    Collection<ItemRequestDto> findAllByUserId(long userId);

    Collection<ItemRequestDto> findAll(long userId);

    ItemRequestDto findById(long requestId, long userId);

    RequestItemsDto create(Item item, ItemRequest itemRequest);

    ItemRequest getbyId(long requestId);
}
