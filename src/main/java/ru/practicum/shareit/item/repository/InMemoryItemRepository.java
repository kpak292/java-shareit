package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("inMemoryItemRepository")
public class InMemoryItemRepository implements ItemRepository {
    private Map<Long, Item> items = new HashMap<>();

    @Override
    public Collection<Item> findAllByUser(long userId) {
        return items.values().stream()
                .filter(item -> item.getUserId() == userId)
                .toList();
    }

    @Override
    public Item findById(long id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException("Item is not found with id - " + id);
        }

        return items.get(id);
    }

    @Override
    public Collection<Item> findByText(String text) {
        if (text.isBlank()) {
            return List.of();
        }

        String pattern = ".*" + text.toLowerCase() + ".*";
        return items.values().stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getName().toLowerCase().matches(pattern) ||
                        item.getDescription().toLowerCase().matches(pattern))
                .toList();
    }

    @Override
    public Item create(Item item) {
        long id = items.keySet().stream().max(Long::compareTo).orElse(0L);
        id++;

        item.setId(id);

        items.put(id, item);
        return item;
    }

    @Override
    public Item update(Item item) {
        return items.put(item.getId(), item);
    }

    @Override
    public Item remove(long id) {
        return items.remove(id);
    }
}
