package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    @Autowired
    private final UserClient userClient;

    @GetMapping()
    public ResponseEntity<Object> getAll() {
        log.info("GET/users");

        return userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        log.info("GET/users/{}", id);

        return userClient.getUserById(id);
    }

    @PostMapping()
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequestDto userDto) {
        log.info("POST/users");

        return userClient.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id, @RequestBody UserRequestDto userDto) {
        log.info("PATCH/users/{}", id);

        return userClient.updateUser(userDto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        log.info("DELETE/users/{}", id);

        return userClient.removeUser(id);
    }
}
