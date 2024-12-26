package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository("inMemoryUserRepository")
public class InMemoryUserRepository implements UserRepository {
    private Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findById(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User is not found with id - " + id);
        }

        return users.get(id);
    }

    @Override
    public User create(User user) {
        if (users.containsValue(user)) {
            throw new DuplicateEmailException("Email already exists in DataBase - " + user.getEmail());
        }

        long id = users.keySet().stream().max(Long::compareTo).orElse(0L);
        id++;

        user.setId(id);

        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        User inMemoryUser = users.get(user.getId());
        users.remove(user.getId());

        if (users.containsValue(user)) {
            users.put(inMemoryUser.getId(), inMemoryUser);
            throw new DuplicateEmailException("Email already exists in DataBase - " + user.getEmail());
        } else {
            users.put(user.getId(), user);
        }

        return user;
    }

    @Override
    public User remove(long id) {
        User user = users.get(id);
        users.remove(id);

        return user;
    }
}
