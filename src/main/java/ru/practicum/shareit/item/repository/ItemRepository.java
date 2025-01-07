package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUserId(long userId);

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String text1, String text2);

    Optional<Item> findByIdAndUserId(long id, long userId);
}
