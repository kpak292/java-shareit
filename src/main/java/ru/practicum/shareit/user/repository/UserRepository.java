package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> findAll();

    User findById(long id);

    User create(User user);

    User update(User user);

    User remove(long id);
}
