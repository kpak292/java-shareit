package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service("userServiceV1")
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier("inMemoryUserRepository")
    private UserRepository userRepository;

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::getUserDto)
                .toList();
    }

    @Override
    public UserDto findById(long id) {
        return UserMapper.INSTANCE.getUserDto(userRepository.findById(id));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.INSTANCE.getUser(userDto);
        return UserMapper.INSTANCE.getUserDto(userRepository.create(user));
    }

    @Override
    public UserDto update(long id, UserDto userDto) {
        User user = userRepository.findById(id);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        return UserMapper.INSTANCE.getUserDto(userRepository.update(user));
    }

    @Override
    public UserDto remove(long id) {
        return UserMapper.INSTANCE.getUserDto(userRepository.remove(id));
    }
}
