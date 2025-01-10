package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Item;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDto> findAllByUser(long userId);

    ItemDto findById(long id, long userId);

    Item findItemById(long id);

    Collection<ItemDto> findByText(String text);

    ItemDto create(ItemDto itemDto, long userId);

    ItemDto update(long id, ItemDto itemDto, long userId);

    ItemDto remove(long id, long userId);

    CommentDto addComment(long itemId, CommentDto commentDto, long userId);
}
