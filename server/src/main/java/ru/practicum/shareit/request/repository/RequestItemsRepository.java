package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.entity.RequestItems;

import java.util.Collection;

public interface RequestItemsRepository extends JpaRepository<RequestItems, Long> {
    Collection<RequestItems> findByRequest(ItemRequest itemRequest);
}
