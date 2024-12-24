package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Collection<Item> findAllByUser(long userId);

    Item findById(long id);

    Collection<Item> findByText(String text);

    Item create(Item item);

    Item update(Item item);

    Item remove(long id);

}
