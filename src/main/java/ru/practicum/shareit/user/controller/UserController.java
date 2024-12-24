package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    @Qualifier("userServiceV1")
    private UserService userService;

    @GetMapping()
    public ResponseEntity<Collection<UserDto>> findAll() {
        log.info("Request: GET/users");

        Collection<UserDto> users = userService.findAll();
        log.info("Response: GET/users with body {}", users);

        return new ResponseEntity<>(
                users,
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        log.info("Request: GET/users/{}", id);

        UserDto userDto = userService.findById(id);
        log.info("Response: GET/users/{} with body {}", id, userDto);

        return new ResponseEntity<>(
                userDto,
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto) {
        log.info("Request: POST/users with body {}", userDto);

        UserDto user = userService.create(userDto);
        log.info("Response: POST/users with body {}", user);

        return new ResponseEntity<>(
                user,
                HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable long id, @RequestBody UserDto userDto) {
        log.info("Request: PATCH/users/{} with body {}", id, userDto);

        UserDto user = userService.update(id, userDto);
        log.info("Response: PATCH/users{} with body {}", id, user);

        return new ResponseEntity<>(
                user,
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> remove(@PathVariable long id) {
        log.info("Request: DELETE/users/{}", id);

        UserDto user = userService.remove(id);
        log.info("Response: DELETE/users/{} with body {}", id, user);

        return new ResponseEntity<>(
                user,
                HttpStatus.OK);
    }
}