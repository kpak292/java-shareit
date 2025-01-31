package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByUserId(long userid);

    List<ItemRequest> findByUserIdNot(long userid);
}
