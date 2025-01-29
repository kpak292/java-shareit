package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByHost(User host);

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String text1, String text2);

    Optional<Item> findByIdAndHost(long id, User host);
}
