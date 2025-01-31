package ru.practicum.shareit.user.controller;

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
        Collection<UserDto> users = userService.findAll();
        log.info("GET/users with response {}", users);

        return new ResponseEntity<>(
                users,
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        UserDto userDto = userService.findById(id);
        log.info("Response: GET/users/{} with response {}", id, userDto);

        return new ResponseEntity<>(
                userDto,
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        UserDto user = userService.create(userDto);
        log.info("POST/users with response {}", user);

        return new ResponseEntity<>(
                user,
                HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable long id, @RequestBody UserDto userDto) {
        UserDto user = userService.update(id, userDto);
        log.info("PATCH/users{} with response {}", id, user);

        return new ResponseEntity<>(
                user,
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> remove(@PathVariable long id) {
        UserDto user = userService.remove(id);
        log.info("DELETE/users/{} with response {}", id, user);

        return new ResponseEntity<>(
                user,
                HttpStatus.OK);
    }
}
