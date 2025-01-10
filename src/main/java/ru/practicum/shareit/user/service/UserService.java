package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> findAll();

    UserDto findById(long id);

    User findUserById(long id);

    UserDto create(UserDto userDto);

    UserDto update(long id, UserDto userDto);

    UserDto remove(long id);
}
