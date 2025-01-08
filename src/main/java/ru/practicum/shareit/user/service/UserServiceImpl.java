package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service("userServiceV1")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::getUserDto)
                .toList();
    }

    @Override
    public UserDto findById(long id) {
        return UserMapper.INSTANCE.getUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User is not found with id = " + id)));
    }

    @Override
    public User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User is not found with id = " + id));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.INSTANCE.getUser(userDto);
        return UserMapper.INSTANCE.getUserDto(userRepository.save(user));
    }

    @Override
    public UserDto update(long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User is not found with id = " + id));

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        return UserMapper.INSTANCE.getUserDto(userRepository.save(user));
    }

    @Override
    public UserDto remove(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User is not found with id = " + id));

        userRepository.deleteById(id);

        return UserMapper.INSTANCE.getUserDto(user);
    }
}
