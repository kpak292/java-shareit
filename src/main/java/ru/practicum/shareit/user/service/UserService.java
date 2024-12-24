package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> findAll();

    UserDto findById(long id);

    UserDto create(UserDto userDto);

    UserDto update(long id, UserDto userDto);

    UserDto remove(long id);
}
