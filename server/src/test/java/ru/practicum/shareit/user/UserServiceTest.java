package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    User user;

    @BeforeEach
    public void dataPreparation() {
        user = User.builder()
                .id(1L)
                .email("test@test.uz")
                .name("name")
                .build();
    }

    @Test
    public void findAllTest() {
        when(userRepository.findAll())
                .thenReturn(List.of(user));


        Collection<UserDto> requestResult = userService.findAll();
        UserDto userDto = UserMapper.INSTANCE.getUserDto(user);
        List<UserDto> expected = List.of(userDto);

        assertArrayEquals(expected.toArray(), requestResult.toArray());

        verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void findUserByIdTest() {
        when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        User requestResult = userService.findUserById(1L);

        assertEquals(user, requestResult);

        verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test
    public void findUserByIdNotFoundTest() {
        when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.findUserById(1L));

        verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test
    public void createTest() {
        when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        UserDto result = userService.create(UserMapper.INSTANCE.getUserDto(user));
        assertEquals(UserMapper.INSTANCE.getUserDto(user), result);

        verify(userRepository, Mockito.times(1))
                .save(Mockito.any(User.class));
    }

    @Test
    public void updateTest() {
        when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        UserDto result = userService.update(1L, UserMapper.INSTANCE.getUserDto(user));
        assertEquals(UserMapper.INSTANCE.getUserDto(user), result);

        verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
        verify(userRepository, Mockito.times(1))
                .save(Mockito.any(User.class));
    }

    @Test
    public void deleteTest() {
        when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        UserDto result = userService.remove(1L);
        assertEquals(UserMapper.INSTANCE.getUserDto(user), result);

        verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
        verify(userRepository, Mockito.times(1))
                .deleteById(Mockito.anyLong());
    }
}